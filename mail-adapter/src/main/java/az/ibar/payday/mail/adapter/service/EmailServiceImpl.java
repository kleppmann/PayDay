package az.ibar.payday.mail.adapter.service;

import az.ibar.payday.mail.adapter.client.UserClient;
import az.ibar.payday.mail.adapter.client.UserDto;
import az.ibar.payday.mail.adapter.model.StockInfo;
import az.ibar.payday.mail.adapter.model.StockNotification;
import az.ibar.payday.mail.adapter.logger.SafeLogger;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final SafeLogger logger = SafeLogger.getLogger(EmailServiceImpl.class);

    private final String sendGridApiKey;
    private final String stockTransactionSubject;
    private final String stockChangeSubject;
    private final String fromAddress;
    private final UserClient userClient;

    public EmailServiceImpl(@Value("${sendgrid.apiKey}") String sendGridApiKey,
                            @Value("${email.subject.stock.transaction}") String stockTransactionSubject,
                            @Value("${email.subject.stock.change}") String stockChangeSubject,
                            @Value("${email.fromAddress}") String fromAddress,
                            UserClient userClient) {
        this.sendGridApiKey = sendGridApiKey;
        this.stockTransactionSubject = stockTransactionSubject;
        this.stockChangeSubject = stockChangeSubject;
        this.fromAddress = fromAddress;
        this.userClient = userClient;
    }

    @Override
    public void sendEmail(StockNotification stockNotification) {

        UserDto userDto = userClient.getUserDto(stockNotification.getPayload().getUserId());

        logger.info("sendEmail.start userDto {}", userDto);

        Email from = new Email(fromAddress);
        Email to = new Email(userDto.getEmail());
        String subject = createSubject(stockNotification);
        String body = createBody(stockNotification);

        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
            logger.info("send email response status {}", response.getStatusCode());
        } catch (Exception ex) {
            throw new RuntimeException("email send problem", ex);
        }
    }

    private String createSubject(StockNotification stockNotification) {
        switch (stockNotification.getEvent()) {
            case STOCK_TRANSACTION:
                return stockTransactionSubject;
            case STOCK_PRICE_CHANGE:
                return stockChangeSubject;
            default:
                throw new IllegalArgumentException("invalid notification event");
        }
    }

    private String createBody(StockNotification stockNotification) {
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
