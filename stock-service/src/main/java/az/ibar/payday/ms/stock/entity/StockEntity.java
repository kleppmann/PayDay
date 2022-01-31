package az.ibar.payday.ms.stock.entity;

import az.ibar.payday.ms.stock.model.enums.Currency;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "stocks")
public class StockEntity extends BaseEntity {
    @NotBlank
    @Column(name = "name")
    private String name;
    @NotNull
    @Column(name = "price")
    private BigDecimal price;
    @NotNull
    @Column(name = "currency")
    @Enumerated(value = EnumType.STRING)
    private Currency currency;
}
