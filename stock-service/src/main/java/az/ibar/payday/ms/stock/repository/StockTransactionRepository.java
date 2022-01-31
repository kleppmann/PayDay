package az.ibar.payday.ms.stock.repository;

import az.ibar.payday.ms.stock.entity.StockTransactionEntity;
import az.ibar.payday.ms.stock.model.enums.TransactionStatus;
import az.ibar.payday.ms.stock.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransactionEntity, Long> {
    StockTransactionEntity findByUserIdAndStockIdAndTypeAndStatus(Long userId, Long stockId,
                                                                  TransactionType transactionType,
                                                                  TransactionStatus status);

    List<StockTransactionEntity> findAllByIdIn(List<Long> ids);

    List<StockTransactionEntity> findAllByStockIdAndTypeAndStatus(Long stockId,
                                                                  TransactionType transactionType,
                                                                  TransactionStatus status);

    List<StockTransactionEntity> findAllByUserIdAndTypeAndStatusAndStockIdIn(Long userId,
                                                                             TransactionType transactionType,
                                                                             TransactionStatus status,
                                                                             List<Long> stockIds);
}
