package az.ibar.payday.ms.stock.model.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockChangeView implements Serializable {

    private String name;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private LocalDateTime dateTime;
}
