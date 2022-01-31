package az.ibar.payday.ms.notification.queue;

import az.ibar.payday.ms.notification.logger.SafeLogger;
import az.ibar.payday.ms.notification.model.StockNotification;
import az.ibar.payday.ms.notification.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class NotificationConsumerQueue {

    private static final SafeLogger logger = SafeLogger.getLogger(NotificationConsumerQueue.class);

    private final ObjectMapper objectMapper;
    private final NotificationService emailService;

    public NotificationConsumerQueue(ObjectMapper objectMapper, NotificationService emailService) {
        this.objectMapper = objectMapper;
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${rabbitmq.notificationQ}")
    public void receiveNotificationToSend(String message) throws IOException {
        var stockNotification = objectMapper.readValue(message, StockNotification.class);
        logger.info("receiveNotificationToSend {}", stockNotification);
        emailService.sendNotification(stockNotification);
    }
}
