package az.ibar.payday.ms.stock.queue;

import az.ibar.payday.ms.stock.model.StockInfo;
import az.ibar.payday.ms.stock.model.StockNotification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationProducerQueue {

    private final String notificationQ;
    private final AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper;

    public NotificationProducerQueue(@Value("${rabbitmq.notificationQ}") String notificationQ,
                                     AmqpTemplate amqpTemplate,
                                     ObjectMapper objectMapper) {
        this.notificationQ = notificationQ;
        this.amqpTemplate = amqpTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendToQueue(StockNotification stockNotification) {
        try {
            amqpTemplate.convertAndSend(notificationQ, objectMapper.writeValueAsString(stockNotification));
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Can't serialize email request", ex);
        }
    }
}
