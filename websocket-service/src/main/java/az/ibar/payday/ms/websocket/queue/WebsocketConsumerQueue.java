package az.ibar.payday.ms.websocket.queue;

import az.ibar.payday.ms.websocket.logger.SafeLogger;
import az.ibar.payday.ms.websocket.model.StockNotification;
import az.ibar.payday.ms.websocket.service.WebsocketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WebsocketConsumerQueue {

    private static final SafeLogger logger = SafeLogger.getLogger(WebsocketConsumerQueue.class);

    private final ObjectMapper objectMapper;
    private final WebsocketService websocketService;

    public WebsocketConsumerQueue(ObjectMapper objectMapper,
                                  WebsocketService websocketService) {
        this.objectMapper = objectMapper;
        this.websocketService = websocketService;
    }

    @RabbitListener(queues = "${rabbitmq.websocketRequestQ}")
    public void receiveNotificationToSend(String message) throws IOException {
        var stockNotification = objectMapper.readValue(message, StockNotification.class);
        logger.info("receiveNotificationToSend via websocket {}", stockNotification);
        websocketService.sendMessage(stockNotification);
    }
}
