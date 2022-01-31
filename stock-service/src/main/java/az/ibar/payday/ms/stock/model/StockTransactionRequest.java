package az.ibar.payday.ms.stock.model;

import az.ibar.payday.ms.stock.model.enums.TransactionType;
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
public class StockTransactionRequest extends Request {

    private Long transactionId;
    @NotNull
    private Long userId;
    @NotNull
    private Long stockId;
    @NotNull
    private TransactionType transactionType;
    @NotNull
    private Amount maximumPrice;
    @NotNull
    private String urlHook;
}
