package az.ibar.payday.ms.notification.service;

import az.ibar.payday.ms.notification.logger.SafeLogger;
import az.ibar.payday.ms.notification.model.StockNotification;
import az.ibar.payday.ms.notification.queue.NotificationConsumerQueue;
import az.ibar.payday.ms.notification.queue.WebsocketProducerQueue;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final SafeLogger logger = SafeLogger.getLogger(NotificationConsumerQueue.class);

    private final WebsocketProducerQueue websocketProducerQueue;

    public NotificationServiceImpl(WebsocketProducerQueue websocketProducerQueue) {
        this.websocketProducerQueue = websocketProducerQueue;
    }

    @Override
    public void sendNotification(StockNotification stockNotification) {
        logger.info("sendNotification {}", stockNotification);
        websocketProducerQueue.sendToQueue(stockNotification);
    }
}
