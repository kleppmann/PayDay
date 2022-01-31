package az.ibar.payday.ms.stock.client;

import az.ibar.payday.ms.stock.model.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockDto implements Serializable {
    @NotBlank
    private String name;
    @NotNull
    private BigDecimal price;
    @NotNull
    private Currency currency;
}
