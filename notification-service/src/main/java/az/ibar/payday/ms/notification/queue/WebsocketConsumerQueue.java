package az.ibar.payday.ms.notification.queue;

import az.ibar.payday.ms.notification.logger.SafeLogger;
import az.ibar.payday.ms.notification.model.StockNotification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WebsocketConsumerQueue {

    private static final SafeLogger logger = SafeLogger.getLogger(WebsocketConsumerQueue.class);

    private final ObjectMapper objectMapper;
    private final EmailProducerQueue emailProducerQueue;

    public WebsocketConsumerQueue(ObjectMapper objectMapper,
                                  EmailProducerQueue emailProducerQueue) {
        this.objectMapper = objectMapper;
        this.emailProducerQueue = emailProducerQueue;
    }

    @RabbitListener(queues = "${rabbitmq.websocketResponseQ}")
    public void receiveNotificationToSend(String message) throws IOException {
        var stockNotification = objectMapper.readValue(message, StockNotification.class);
        logger.info("receiveNotificationToSend to send email adapter {}", stockNotification);
        emailProducerQueue.sendToEmailQueue(stockNotification);
    }
}
