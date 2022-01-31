package az.ibar.payday.ms.stock.repository;

import az.ibar.payday.ms.stock.entity.StockChangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockChangeRepository extends JpaRepository<StockChangeEntity, Long> {
    Optional<StockChangeEntity> findByStockIdAndDateTimeBetween(Long stockId, LocalDateTime begin, LocalDateTime end);

    List<StockChangeEntity> findAllByStockIdAndDateTimeBetween(Long stockId, LocalDateTime begin, LocalDateTime end);

}
