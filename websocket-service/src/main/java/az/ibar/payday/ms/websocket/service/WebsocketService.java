package az.ibar.payday.ms.websocket.service;

import az.ibar.payday.ms.websocket.model.StockNotification;
import az.ibar.payday.ms.websocket.model.enums.NotificationStatus;

public interface WebsocketService {
    void sendMessage(StockNotification notification);
}
