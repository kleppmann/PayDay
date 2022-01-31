package az.ibar.payday.ms.stock.service.impl;

import az.ibar.payday.ms.stock.client.StockDto;
import az.ibar.payday.ms.stock.client.StockListClient;
import az.ibar.payday.ms.stock.entity.StockChangeEntity;
import az.ibar.payday.ms.stock.entity.StockEntity;
import az.ibar.payday.ms.stock.entity.StockTransactionEntity;
import az.ibar.payday.ms.stock.entity.UserStockEntity;
import az.ibar.payday.ms.stock.exception.NotFoundException;
import az.ibar.payday.ms.stock.helper.CacheHelper;
import az.ibar.payday.ms.stock.helper.PercentageHelper;
import az.ibar.payday.ms.stock.logger.SafeLogger;
import az.ibar.payday.ms.stock.model.StockInfo;
import az.ibar.payday.ms.stock.model.StockNotification;
import az.ibar.payday.ms.stock.model.enums.Currency;
import az.ibar.payday.ms.stock.model.enums.NotificationEvent;
import az.ibar.payday.ms.stock.model.enums.UserStockChange;
import az.ibar.payday.ms.stock.queue.NotificationProducerQueue;
import az.ibar.payday.ms.stock.repository.StockChangeRepository;
import az.ibar.payday.ms.stock.repository.StockRepository;
import az.ibar.payday.ms.stock.repository.StockTransactionRepository;
import az.ibar.payday.ms.stock.repository.UserStockRepository;
import az.ibar.payday.ms.stock.service.StockListService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockListServiceImpl implements StockListService {

    private static final SafeLogger logger = SafeLogger.getLogger(StockListServiceImpl.class);

    private final StockRepository stockRepository;
    private final StockTransactionRepository transactionRepository;
    private final NotificationProducerQueue notificationProducerQueue;
    private final StockListClient stockListClient;
    private final PercentageHelper percentageHelper;
    private final UserStockRepository userStockRepository;
    private final CacheHelper cacheHelper;
    private final StockChangeRepository stockChangeRepository;

    public StockListServiceImpl(StockRepository stockRepository,
                                StockTransactionRepository transactionRepository,
                                NotificationProducerQueue messageProducer,
                                StockListClient stockListClient,
                                PercentageHelper percentageHelper,
                                UserStockRepository userStockRepository,
                                CacheHelper cacheHelper,
                                StockChangeRepository stockChangeRepository) {
        this.stockRepository = stockRepository;
        this.transactionRepository = transactionRepository;
        this.notificationProducerQueue = messageProducer;
        this.stockListClient = stockListClient;
        this.percentageHelper = percentageHelper;
        this.userStockRepository = userStockRepository;
        this.cacheHelper = cacheHelper;
        this.stockChangeRepository = stockChangeRepository;
    }

    public List<StockDto> retrieveAvailableStocks(Currency currency) {
        List<StockDto> availableStocks = new ArrayList<>();
        switch (currency) {
            case AED:
                availableStocks = stockListClient.getDubaiStocks();
                cacheHelper.cacheStockDtos(cacheHelper.getStockBucketKey(Currency.AED.name()), availableStocks);
                break;
            case EUR:
                availableStocks = stockListClient.getMilanStocks();
                cacheHelper.cacheStockDtos(cacheHelper.getStockBucketKey(Currency.EUR.name()), availableStocks);
                break;
            case USD:
                availableStocks = stockListClient.getNYStocks();
                cacheHelper.cacheStockDtos(cacheHelper.getStockBucketKey(Currency.USD.name()), availableStocks);
                break;
            default:
                break;
        }

        availableStocks.forEach(this::saveStock);
        return availableStocks;
    }

    private void saveStock(StockDto stock) {
        StockEntity stockEntity = stockRepository.findByName(stock.getName()).orElse(
                StockEntity
                        .builder()
                        .currency(stock.getCurrency())
                        .name(stock.getName())
                        .build());

        stockEntity.setPrice(stock.getPrice());
        stockRepository.save(stockEntity);

        checkGainOrLoss(stockEntity);
        saveStockChange(stockEntity);
    }

    private void checkGainOrLoss(StockEntity stockEntity) {

        List<UserStockEntity> userStocks = userStockRepository.findAllByStockId(stockEntity.getId());

        List<Long> transactionIds = userStocks
                .stream()
                .map(UserStockEntity::getTransactionId).collect(Collectors.toList());

        List<StockTransactionEntity> stockTransactions = transactionRepository.findAllByIdIn(transactionIds);

        userStocks
                .forEach(userStock -> {

                    BigDecimal currentPrice = stockEntity.getPrice();

                    StockTransactionEntity stockTransaction = stockTransactions
                            .stream()
                            .filter(t -> t.getId().equals(userStock.getTransactionId()))
                            .findAny()
                            .orElseThrow(() -> new NotFoundException(
                                    "transaction not found with id: " + userStock.getTransactionId()));

                    BigDecimal buyingPrice = stockTransaction.getPrice();

                    StockInfo stockInfo = StockInfo
                            .builder()
                            .buyingPrice(buyingPrice)
                            .currentPrice(currentPrice)
                            .stockId(stockEntity.getId())
                            .stockName(stockEntity.getName())
                            .percentage(percentageHelper.calculatePercentage(buyingPrice, currentPrice))
                            .userId(stockTransaction.getUserId())
                            .build();

                    StockNotification stockNotification = StockNotification
                            .builder()
                            .event(NotificationEvent.STOCK_PRICE_CHANGE)
                            .payload(stockInfo)
                            .build();

                    if (currentPrice.divide(buyingPrice, 2, RoundingMode.HALF_UP)
                            .compareTo(BigDecimal.valueOf(1.5)) >= 0) {
                        stockInfo.setChange(UserStockChange.GAIN);
                    } else if (buyingPrice.divide(currentPrice, 2, RoundingMode.HALF_UP)
                            .compareTo(BigDecimal.valueOf(2)) >= 0) {
                        stockInfo.setChange(UserStockChange.LOSS);
                    } else return;

                    notificationProducerQueue.sendToQueue(stockNotification);
                });
    }

    private void saveStockChange(StockEntity stock) {

        LocalDateTime todayBegin = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        var stockChange = stockChangeRepository
                .findByStockIdAndDateTimeBetween(stock.getId(), todayBegin, todayEnd).orElse(
                        StockChangeEntity.builder().dateTime(LocalDateTime.now()).maxPrice(stock.getPrice())
                                .minPrice(stock.getPrice()).stockId(stock.getId()).build());

        if (stock.getPrice().compareTo(stockChange.getMaxPrice()) > 0)
            stockChange.setMaxPrice(stock.getPrice());

        if (stock.getPrice().compareTo(stockChange.getMinPrice()) < 0)
            stockChange.setMinPrice(stock.getPrice());

        stockChangeRepository.save(stockChange);
    }
}

