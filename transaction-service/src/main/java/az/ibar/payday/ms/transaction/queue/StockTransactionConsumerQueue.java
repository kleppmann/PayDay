package az.ibar.payday.ms.transaction.queue;

import az.ibar.payday.ms.transaction.logger.SafeLogger;
import az.ibar.payday.ms.transaction.model.StockTransactionRequest;
import az.ibar.payday.ms.transaction.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class StockTransactionConsumerQueue {

    private static final SafeLogger logger = SafeLogger.getLogger(StockTransactionConsumerQueue.class);
    private final ObjectMapper objectMapper;
    private final TransactionService transactionService;

    public StockTransactionConsumerQueue(ObjectMapper objectMapper, TransactionService transactionService) {
        this.objectMapper = objectMapper;
        this.transactionService = transactionService;
    }

    @RabbitListener(queues = "${rabbitmq.stockTransactionRequestQ}")
    public void receiveStockTransactionRequest(String message) throws IOException {
        var stockTransactionRequest = objectMapper.readValue(message, StockTransactionRequest.class);
        logger.info("receiveStockTransactionRequest.start {}", stockTransactionRequest);
        transactionService.execute(stockTransactionRequest);
        logger.info("receiveStockTransactionRequest.end {}", stockTransactionRequest);
    }
}
