package az.ibar.payday.ms.stock.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users_stocks")
public class UserStockEntity extends BaseEntity {

    @NotNull
    @Column(name = "user_id")
    private Long userId;
    @NotNull
    @Column(name = "stock_id")
    private Long stockId;
    @NotNull
    @Column(name = "transaction_id")
    private Long transactionId;

}
