package az.ibar.payday.ms.stock.model.view;

import az.ibar.payday.ms.stock.model.Amount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockView implements Serializable {

    private String name;
    private Amount price;
}
