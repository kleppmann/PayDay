package az.ibar.payday.ms.notification.queue;

import az.ibar.payday.ms.notification.model.StockNotification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WebsocketProducerQueue {

    private final String websocketRequestQ;
    private final AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper;

    public WebsocketProducerQueue(@Value("${rabbitmq.websocketRequestQ}") String websocketRequestQ,
                                  AmqpTemplate amqpTemplate,
                                  ObjectMapper objectMapper) {
        this.websocketRequestQ = websocketRequestQ;
        this.amqpTemplate = amqpTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendToQueue(StockNotification stockNotification) {
        try {
            amqpTemplate.convertAndSend(websocketRequestQ, objectMapper.writeValueAsString(stockNotification));
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Can't serialize email request", ex);
        }
    }
}
