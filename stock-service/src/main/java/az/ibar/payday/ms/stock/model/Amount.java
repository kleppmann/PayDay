package az.ibar.payday.ms.stock.model;

import az.ibar.payday.ms.stock.model.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Amount implements Serializable {
    private BigDecimal value;
    private Currency currency;
}
