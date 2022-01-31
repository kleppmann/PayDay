package az.ibar.payday.ms.stock.queue;

import az.ibar.payday.ms.stock.model.StockTransactionRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StockTransactionProducerQueue {

    private final String stockTransactionRequestQ;
    private final AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper;

    public StockTransactionProducerQueue(@Value("${rabbitmq.stockTransactionRequestQ}") String stockTransactionRequestQ,
                                         AmqpTemplate amqpTemplate,
                                         ObjectMapper objectMapper) {
        this.stockTransactionRequestQ = stockTransactionRequestQ;
        this.amqpTemplate = amqpTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendToTransactionQueue(StockTransactionRequest transactionRequest) {
        log.info("sendToTransactionQueue {}", transactionRequest);
        try {
            amqpTemplate.convertAndSend(stockTransactionRequestQ, objectMapper.writeValueAsString(transactionRequest));
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Can't serialize email request", ex);
        }
    }
}
