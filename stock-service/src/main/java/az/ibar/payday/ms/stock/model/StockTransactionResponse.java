package az.ibar.payday.ms.stock.model;

import az.ibar.payday.ms.stock.model.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class StockTransactionResponse extends Response {

    @NotNull
    private Long transactionId;
    @NotNull
    private TransactionStatus status;
}
