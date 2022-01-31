package az.ibar.payday.stock.adapter.model;

import az.ibar.payday.stock.adapter.model.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockDto {
    private String name;
    private BigDecimal price;
    private Currency currency;
}
