package az.ibar.payday.ms.transaction.service.impl;

import az.ibar.payday.ms.transaction.client.StockClient;
import az.ibar.payday.ms.transaction.logger.SafeLogger;
import az.ibar.payday.ms.transaction.model.StockTransactionRequest;
import az.ibar.payday.ms.transaction.model.StockTransactionResponse;
import az.ibar.payday.ms.transaction.model.enums.TransactionStatus;
import az.ibar.payday.ms.transaction.queue.StockTransactionConsumerQueue;
import az.ibar.payday.ms.transaction.service.TransactionService;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final SafeLogger logger = SafeLogger.getLogger(TransactionServiceImpl.class);
    private final StockClient stockClient;

    public TransactionServiceImpl(StockClient stockClient) {
        this.stockClient = stockClient;
    }

    @Override
    public void execute(StockTransactionRequest transactionRequest) {
        logger.info("transaction execute start {}", transactionRequest);

        StockTransactionResponse transactionResponse = StockTransactionResponse
                .builder()
                .transactionId(transactionRequest.getTransactionId())
                .requestId(transactionRequest.getRequestId())
                .build();
        try {
            Thread.sleep(1000);
            // assume that transaction is successful
            transactionResponse.setStatus(TransactionStatus.SUCCESS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("transaction execute error", e);
            transactionResponse.setStatus(TransactionStatus.FAIL);
            transactionResponse.setErrorMessage(e.getMessage());
        }

        stockClient.transactionResult(transactionResponse);
        logger.info("transaction execute end {}", transactionRequest);
    }
}
