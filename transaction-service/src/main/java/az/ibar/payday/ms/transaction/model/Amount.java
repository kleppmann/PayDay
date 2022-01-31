package az.ibar.payday.ms.transaction.model;

import az.ibar.payday.ms.transaction.model.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Amount {
    private BigDecimal value;
    private Currency currency;
}
