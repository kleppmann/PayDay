package az.ibar.payday.mail.adapter.model;

import az.ibar.payday.mail.adapter.model.enums.TransactionStatus;
import az.ibar.payday.mail.adapter.model.enums.TransactionType;
import az.ibar.payday.mail.adapter.model.enums.UserStockChange;
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
