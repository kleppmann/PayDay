package az.ibar.payday.ms.stock.repository;

import az.ibar.payday.ms.stock.entity.StockEntity;
import az.ibar.payday.ms.stock.model.enums.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {
    Optional<StockEntity> findByName(String name);

    List<StockEntity> findAllByIdIn(List<Long> ids);

    List<StockEntity> findAllByCurrency(Currency currency);

    List<StockEntity> findAllByPriceGreaterThan(BigDecimal price);
}
