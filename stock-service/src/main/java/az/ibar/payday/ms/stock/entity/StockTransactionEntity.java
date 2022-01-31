package az.ibar.payday.ms.stock.entity;

import az.ibar.payday.ms.stock.model.enums.Currency;
import az.ibar.payday.ms.stock.model.enums.TransactionStatus;
import az.ibar.payday.ms.stock.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "stock_transactions")
public class StockTransactionEntity extends BaseEntity {

    @NotNull
    @Column(name = "stock_id")
    private Long stockId;
    @NotNull
    @Column(name = "user_id")
    private Long userId;
    @NotNull
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    @NotNull
    @Column(name = "price")
    private BigDecimal price;
    @NotNull
    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Column(name = "error_message")
    private String errorMessage;
}
