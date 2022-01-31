package az.ibar.payday.ms.stock.service.impl;

import az.ibar.payday.ms.stock.entity.StockTransactionEntity;
import az.ibar.payday.ms.stock.entity.UserStockEntity;
import az.ibar.payday.ms.stock.exception.BadRequestException;
import az.ibar.payday.ms.stock.exception.NotFoundException;
import az.ibar.payday.ms.stock.logger.SafeLogger;
import az.ibar.payday.ms.stock.model.HeaderKeys;
import az.ibar.payday.ms.stock.model.StockInfo;
import az.ibar.payday.ms.stock.model.StockNotification;
import az.ibar.payday.ms.stock.model.StockTransactionRequest;
import az.ibar.payday.ms.stock.model.StockTransactionResponse;
import az.ibar.payday.ms.stock.model.enums.NotificationEvent;
import az.ibar.payday.ms.stock.model.enums.TransactionStatus;
import az.ibar.payday.ms.stock.model.enums.TransactionType;
import az.ibar.payday.ms.stock.queue.NotificationProducerQueue;
import az.ibar.payday.ms.stock.queue.StockTransactionProducerQueue;
import az.ibar.payday.ms.stock.repository.StockRepository;
import az.ibar.payday.ms.stock.repository.StockTransactionRepository;
import az.ibar.payday.ms.stock.repository.UserStockRepository;
import az.ibar.payday.ms.stock.service.StockTransactionService;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockTransactionServiceImpl implements StockTransactionService {

    private static final SafeLogger logger = SafeLogger.getLogger(StockTransactionServiceImpl.class);

    private final StockTransactionRepository transactionRepository;
    private final UserStockRepository userStockRepository;
    private final NotificationProducerQueue notificationProducerQueue;
    private final StockTransactionProducerQueue stockTransactionProducerQueue;
    private final StockRepository stockRepository;

    public StockTransactionServiceImpl(StockTransactionRepository transactionRepository,
                                       UserStockRepository userStockRepository,
                                       NotificationProducerQueue notificationProducerQueue,
                                       StockTransactionProducerQueue stockTransactionProducerQueue,
                                       StockRepository stockRepository) {
        this.transactionRepository = transactionRepository;
        this.userStockRepository = userStockRepository;
        this.notificationProducerQueue = notificationProducerQueue;
        this.stockTransactionProducerQueue = stockTransactionProducerQueue;
        this.stockRepository = stockRepository;
    }

    @Override
    public void executeTransaction(StockTransactionRequest transactionRequest) {
        logger.info("executeTransaction {}", transactionRequest);

        checkStockAvailability(transactionRequest);

        var transactionEntity = StockTransactionEntity
                .builder()
                .userId(transactionRequest.getUserId())
                .stockId(transactionRequest.getStockId())
                .type(transactionRequest.getTransactionType())
                .currency(transactionRequest.getMaximumPrice().getCurrency())
                .price(transactionRequest.getMaximumPrice().getValue())
                .status(TransactionStatus.PROCESSING)
                .build();

        transactionEntity = transactionRepository.save(transactionEntity);
        transactionRequest.setTransactionId(transactionEntity.getId());
        transactionRequest.setRequestId(MDC.get(HeaderKeys.HEADER_REQUEST_ID));

        stockTransactionProducerQueue.sendToTransactionQueue(transactionRequest);
    }

    private void checkStockAvailability(StockTransactionRequest transactionRequest) {

        if (transactionRequest.getTransactionType().equals(TransactionType.SELL)) {
            List<UserStockEntity> userStocks = userStockRepository.findAllByUserId(transactionRequest.getUserId());
            boolean isExists = userStocks
                    .stream()
                    .anyMatch(userStock -> userStock.getStockId().equals(transactionRequest.getStockId()));

            if (!isExists)
                throw new NotFoundException(
                        String.format("stock with id %1$s not exists in user", transactionRequest.getStockId()));
        } else {
            var stockEntity = stockRepository.findById(transactionRequest.getStockId())
                    .orElseThrow(() -> new NotFoundException(String.format(
                            "stock with id %1$s not found", transactionRequest.getStockId())));

            if (!transactionRequest.getMaximumPrice().getCurrency().equals(stockEntity.getCurrency()))
                throw new BadRequestException("wrong currency");

            if (transactionRequest.getMaximumPrice().getValue().compareTo(stockEntity.getPrice()) < 0)
                throw new BadRequestException("stock price is higher than your amount");
        }
    }

    @Transactional
    @Override
    public void executeTransactionResult(StockTransactionResponse transactionResponse) {
        logger.info("executeTransactionResult.start {}", transactionResponse);

        final long transactionId = transactionResponse.getTransactionId();
        var transactionEntity = transactionRepository.findById(transactionResponse.getTransactionId())
                .orElseThrow(() -> new NotFoundException(
                        String.format("transaction with id %1$s not found", transactionId)));

        var stockEntity = stockRepository.findById(transactionEntity.getStockId())
                .orElseThrow(() -> new NotFoundException(String.format(
                        "stock with id %1$s not found", transactionEntity.getStockId())));

        StockInfo stockInfo = StockInfo
                .builder()
                .userId(transactionEntity.getUserId())
                .stockId(transactionEntity.getStockId())
                .type(transactionEntity.getType())
                .status(transactionResponse.getStatus())
                .stockName(stockEntity.getName())
                .build();

        StockNotification stockNotification = StockNotification
                .builder()
                .payload(stockInfo)
                .event(NotificationEvent.STOCK_TRANSACTION)
                .build();

        try {

            if (transactionResponse.getStatus().equals(TransactionStatus.SUCCESS)) {
                transactionResponse = updateUserStock(transactionEntity);
            }

            transactionEntity.setStatus(transactionResponse.getStatus());
            transactionEntity.setErrorMessage(transactionResponse.getErrorMessage());
            stockInfo.setStatus(transactionResponse.getStatus());
        } catch (Exception e) {
            logger.error("executeTransactionResult.error", e);
            stockInfo.setStatus(TransactionStatus.FAIL);
            transactionEntity.setStatus(TransactionStatus.FAIL);
        } finally {
            transactionRepository.save(transactionEntity);
            notificationProducerQueue.sendToQueue(stockNotification);
        }

        logger.info("executeTransactionResult.end {}", transactionResponse);
    }

    private synchronized StockTransactionResponse updateUserStock(StockTransactionEntity transactionEntity) {

        var userStockEntityOptional = userStockRepository
                .findByUserIdAndStockId(transactionEntity.getUserId(), transactionEntity.getStockId());

        if (transactionEntity.getType().equals(TransactionType.BUY)) {

            if (userStockEntityOptional.isPresent())
                return StockTransactionResponse.builder()
                        .status(TransactionStatus.FAIL)
                        .errorMessage(
                                String.format("stock with id %1$s already bought", transactionEntity.getStockId()))
                        .build();

            var userStockEntity = UserStockEntity
                    .builder()
                    .stockId(transactionEntity.getStockId())
                    .userId(transactionEntity.getUserId())
                    .transactionId(transactionEntity.getId())
                    .build();
            userStockRepository.save(userStockEntity);
        } else {
            if (userStockEntityOptional.isEmpty()) {
                return StockTransactionResponse.builder()
                        .status(TransactionStatus.FAIL)
                        .errorMessage(String.format("stock with id %1$s doesn't exist", transactionEntity.getStockId()))
                        .build();
            }
            userStockRepository.delete(userStockEntityOptional.get());
        }

        return StockTransactionResponse.builder().status(TransactionStatus.SUCCESS).build();
    }
}
