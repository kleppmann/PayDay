package az.ibar.payday.ms.notification.service;

import az.ibar.payday.ms.notification.model.StockNotification;

public interface NotificationService {
    void sendNotification(StockNotification stockNotification);
}
