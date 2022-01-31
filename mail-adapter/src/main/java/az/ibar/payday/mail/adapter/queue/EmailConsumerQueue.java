package az.ibar.payday.mail.adapter.queue;

import az.ibar.payday.mail.adapter.logger.SafeLogger;
import az.ibar.payday.mail.adapter.model.StockNotification;
import az.ibar.payday.mail.adapter.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EmailConsumerQueue {

    private static final SafeLogger logger = SafeLogger.getLogger(EmailConsumerQueue.class);

    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    public EmailConsumerQueue(ObjectMapper objectMapper, EmailService emailService) {
        this.objectMapper = objectMapper;
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${rabbitmq.emailQ}")
    public void receiveNotificationToSend(String message) throws IOException {
        var stockNotification = objectMapper.readValue(message, StockNotification.class);
        logger.info("receiveNotificationToSend {}", stockNotification);
        emailService.sendEmail(stockNotification);
    }
}
