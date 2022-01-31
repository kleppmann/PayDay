package az.ibar.payday.mail.adapter.model;

import az.ibar.payday.mail.adapter.model.enums.NotificationEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockNotification {
    private NotificationEvent event;
    private StockInfo payload;
}
