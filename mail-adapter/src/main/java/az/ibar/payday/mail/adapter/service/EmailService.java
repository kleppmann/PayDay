package az.ibar.payday.mail.adapter.service;

import az.ibar.payday.mail.adapter.model.StockNotification;

public interface EmailService {
    void sendEmail(StockNotification stockNotification);
}
