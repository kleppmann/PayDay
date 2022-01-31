package az.ibar.payday.ms.websocket.service.impl;

import az.ibar.payday.ms.websocket.client.UserClient;
import az.ibar.payday.ms.websocket.helper.CacheHelper;
import az.ibar.payday.ms.websocket.logger.SafeLogger;
import az.ibar.payday.ms.websocket.model.StockInfo;
import az.ibar.payday.ms.websocket.model.StockNotification;
import az.ibar.payday.ms.websocket.queue.WebsocketProducerQueue;
import az.ibar.payday.ms.websocket.service.WebsocketService;
import org.redisson.api.RBucket;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebsocketServiceImpl implements WebsocketService {

    private static final SafeLogger logger = SafeLogger.getLogger(WebsocketServiceImpl.class);

    private final SimpMessagingTemplate broker;
    private final CacheHelper cacheHelper;
    private final UserClient userClient;
    private final WebsocketProducerQueue producerQueue;

    public WebsocketServiceImpl(SimpMessagingTemplate broker,
                                CacheHelper cacheHelper, UserClient userClient,
                                WebsocketProducerQueue producerQueue) {
        this.broker = broker;
        this.cacheHelper = cacheHelper;
        this.userClient = userClient;
        this.producerQueue = producerQueue;
    }

    @Override
    public void sendMessage(StockNotification notification) {

        logger.info("websocket send notification start {}", notification);
        try {

            var user = userClient.getUserDto(notification.getPayload().getUserId());
            logger.info("websocket send notification user info {}", user);

            RBucket<String> bucket = cacheHelper.getBucket(cacheHelper.getWebsocketBucketKey(user.getUsername()));

            if (bucket.get() != null) {
                broker.convertAndSend("/topic/greetings", createMessageBody(notification));
                logger.info("user is online message sent via web socket");
            } else {
                logger.info("user is offline message will send via mail");
                producerQueue.sendToQueue(notification);
            }
        } catch (Exception e) {
            logger.error("error during send notification via web socket", e);
            producerQueue.sendToQueue(notification);
        }

        logger.info("websocket send notification end {}", notification);
    }

    private String createMessageBody(StockNotification stockNotification) {
        StockInfo stockInfo = stockNotification.getPayload();
        switch (stockNotification.getEvent()) {
            case STOCK_TRANSACTION:
                return String.format("For %1$s, %2$s transaction status is %3$s",
                        stockInfo.getStockName(), stockInfo.getType().name(), stockInfo.getStatus().name());
            case STOCK_PRICE_CHANGE:
                return String.format("For %1$s, price changed:\n" +
                                "Buying price is %2$s\n" +
                                "Current price is %3$s\n" +
                                "Change is %4$s %5$s",
                        stockInfo.getStockName(), stockInfo.getBuyingPrice(), stockInfo.getCurrentPrice(),
                        stockInfo.getPercentage(), stockInfo.getChange().name());
            default:
                throw new IllegalArgumentException("invalid notification event");
        }
    }
}
