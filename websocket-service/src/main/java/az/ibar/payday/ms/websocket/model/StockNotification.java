package az.ibar.payday.ms.websocket.model;

import az.ibar.payday.ms.websocket.model.enums.NotificationEvent;
import az.ibar.payday.ms.websocket.model.enums.NotificationStatus;
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
    private NotificationStatus status;
    private StockInfo payload;
}
