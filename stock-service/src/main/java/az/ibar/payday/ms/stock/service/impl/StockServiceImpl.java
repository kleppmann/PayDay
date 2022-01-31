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
import az.ibar.payday.ms.stock.mapper.StockChangeMapper;
import az.ibar.payday.ms.stock.mapper.StockMapper;
import az.ibar.payday.ms.stock.model.Amount;
import az.ibar.payday.ms.stock.model.enums.TransactionStatus;
import az.ibar.payday.ms.stock.model.enums.TransactionType;
import az.ibar.payday.ms.stock.model.view.StockChangeView;
import az.ibar.payday.ms.stock.model.view.StockView;
import az.ibar.payday.ms.stock.model.view.UserStockView;
import az.ibar.payday.ms.stock.repository.StockChangeRepository;
import az.ibar.payday.ms.stock.repository.StockRepository;
import az.ibar.payday.ms.stock.repository.StockTransactionRepository;
import az.ibar.payday.ms.stock.repository.UserStockRepository;
import az.ibar.payday.ms.stock.service.StockService;
import org.redisson.api.RBucket;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl implements StockService {

    private static final SafeLogger logger = SafeLogger.getLogger(StockServiceImpl.class);

    private final StockRepository stockRepository;
    private final StockTransactionRepository transactionRepository;
    private final PercentageHelper percentageHelper;
    private final UserStockRepository userStockRepository;
    private final StockListClient stockListClient;
    private final StockChangeRepository stockChangeRepository;
    private final CacheHelper cacheHelper;

    public StockServiceImpl(StockRepository stockRepository,
                            StockTransactionRepository transactionRepository,
                            PercentageHelper percentageHelper,
                            UserStockRepository userStockRepository,
                            StockListClient stockListClient,
                            StockChangeRepository stockChangeRepository,
                            CacheHelper cacheHelper) {
        this.stockRepository = stockRepository;
        this.transactionRepository = transactionRepository;
        this.percentageHelper = percentageHelper;
        this.userStockRepository = userStockRepository;
        this.stockListClient = stockListClient;
        this.stockChangeRepository = stockChangeRepository;
        this.cacheHelper = cacheHelper;
    }

    @Override
    public List<StockView> findAll() {

        logger.info("findAll stocks start");
        List<StockDto> allStocks = new ArrayList<>(stockListClient.getDubaiStocks());
        allStocks.addAll(stockListClient.getMilanStocks());
        allStocks.addAll(stockListClient.getNYStocks());

        logger.info("findAll stocks end");
        return allStocks
                .stream()
                .map(StockMapper.INSTANCE::stockViewFromDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockView> findAllPreferential() {
        logger.info("findAllPreferential stocks start");
        RBucket<List<StockView>> preferentialBucket = cacheHelper
                .getBucket(cacheHelper.getPreferentialStockBucketKey("preferential"));

        if (preferentialBucket.get() == null) return calculatePreferential();

        logger.info("findAllPreferential stocks end");
        return preferentialBucket.get();
    }

    @Override
    public List<StockChangeView> getStockVolatility(Long id) {

        logger.info("calculatePreferential start stock id {}", id);
        StockEntity stock = stockRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("stock with id %1$s not found", id)));

        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusMonths(2);
        LocalDateTime endTime = LocalDateTime.now();
        List<StockChangeEntity> stockChanges = stockChangeRepository
                .findAllByStockIdAndDateTimeBetween(stock.getId(), startTime, endTime);

        logger.info("calculatePreferential end stock id {}", id);
        return stockChanges
                .stream()
                .map(stockChange -> StockChangeMapper.INSTANCE.stockChangeViewFromEntity(stockChange, stock))
                .collect(Collectors.toList());
    }

    @Override
    public List<StockView> calculatePreferential() {

        logger.info("calculatePreferential start");
        List<StockView> stockViews = new ArrayList<>();
        List<StockEntity> allStocks = stockRepository.findAllByPriceGreaterThan(BigDecimal.valueOf(3));

        allStocks.forEach(stock -> {

            LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(5);
            LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

            List<StockChangeEntity> stockChanges = stockChangeRepository
                    .findAllByStockIdAndDateTimeBetween(stock.getId(), startTime, endTime);

            boolean isPreferential = true;
            for (var stockChange : stockChanges) {
                BigDecimal dailyDiff = percentageHelper
                        .calculatePercentage(stockChange.getMinPrice(), stockChange.getMaxPrice());

                if (dailyDiff.compareTo(BigDecimal.valueOf(5)) > 0) {
                    isPreferential = false;
                    break;
                }
            }

            if (isPreferential) {
                stockViews.add(StockMapper.INSTANCE.stockViewFromEntity(stock));
            }
        });

        cacheHelper.cacheStockViews(cacheHelper.getPreferentialStockBucketKey("preferential"), stockViews);

        logger.info("calculatePreferential end");
        return stockViews;
    }

    @Override
    public List<UserStockView> findUserStocks(Long userId) {

        logger.info("findUserStocks start");
        List<UserStockEntity> userStockEntities = userStockRepository.findAllByUserId(userId);

        List<Long> stockIds = userStockEntities
                .stream()
                .map(UserStockEntity::getStockId).collect(Collectors.toList());

        List<StockTransactionEntity> stockTransactions = transactionRepository
                .findAllByUserIdAndTypeAndStatusAndStockIdIn(userId, TransactionType.BUY, TransactionStatus.SUCCESS,
                        stockIds);

        List<StockEntity> stocks = stockRepository.findAllByIdIn(stockIds);

        logger.info("findUserStocks end");
        return getUserStockViews(userStockEntities, stockTransactions, stocks);
    }

    private List<UserStockView> getUserStockViews(List<UserStockEntity> userStockEntities,
                                                  List<StockTransactionEntity> stockTransactions,
                                                  List<StockEntity> stocks) {

        return
                userStockEntities
                        .stream()
                        .map(userStock -> {
                            StockEntity stock = stocks
                                    .stream()
                                    .filter(s -> s.getId().equals(userStock.getStockId()))
                                    .findAny()
                                    .orElseThrow(() -> new NotFoundException(String.format(
                                            "stock not found with id %1$s", userStock.getStockId())));

                            StockTransactionEntity stockTransaction = stockTransactions
                                    .stream()
                                    .filter(t -> t.getId().equals(userStock.getTransactionId()))
                                    .findAny()
                                    .orElseThrow(() -> new NotFoundException(String.format(
                                            "transaction not found with id %1$s ", userStock.getTransactionId())));

                            Amount currentPrice = Amount
                                    .builder()
                                    .currency(stock.getCurrency())
                                    .value(stock.getPrice())
                                    .build();

                            Amount buyingPrice = Amount
                                    .builder()
                                    .currency(stockTransaction.getCurrency())
                                    .value(stockTransaction.getPrice())
                                    .build();

                            String percentage = currentPrice.getValue()
                                    .compareTo(buyingPrice.getValue()) < 0 ? "-" : "+";
                            percentage = percentage.concat(percentageHelper
                                    .calculatePercentage(buyingPrice.getValue(), currentPrice.getValue()).toString());

                            return UserStockView
                                    .builder()
                                    .stockId(stock.getId())
                                    .currentPrice(currentPrice)
                                    .buyingPrice(buyingPrice)
                                    .percentage(percentage)
                                    .name(stock.getName())
                                    .build();
                        }).collect(Collectors.toList());
    }
}
