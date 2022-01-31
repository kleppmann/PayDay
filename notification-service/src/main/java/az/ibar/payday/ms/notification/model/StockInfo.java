package az.ibar.payday.ms.notification.model;

import az.ibar.payday.ms.notification.model.enums.TransactionStatus;
import az.ibar.payday.ms.notification.model.enums.TransactionType;
import az.ibar.payday.ms.notification.model.enums.UserStockChange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockInfo {

    private Long userId;
    private Long stockId;
    private String stockName;
    private BigDecimal currentPrice;
    private BigDecimal buyingPrice;
    private BigDecimal sellingPrice;
    private BigDecimal percentage;
    private TransactionType type;
    private TransactionStatus status;
    private UserStockChange change;
}
