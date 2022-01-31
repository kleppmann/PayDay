package az.ibar.payday.ms.stock.model;

import az.ibar.payday.ms.stock.model.enums.NotificationEvent;
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
