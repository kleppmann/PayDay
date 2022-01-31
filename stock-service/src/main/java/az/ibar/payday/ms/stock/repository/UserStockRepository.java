package az.ibar.payday.ms.stock.repository;

import az.ibar.payday.ms.stock.entity.UserStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserStockRepository extends JpaRepository<UserStockEntity, Long> {
    Optional<UserStockEntity> findByUserIdAndStockId(Long userId, Long stockId);

    List<UserStockEntity> findAllByUserId(Long userId);

    List<UserStockEntity> findAllByStockId(Long stockId);
}
