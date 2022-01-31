package az.ibar.payday.ms.stock.service;

import az.ibar.payday.ms.stock.model.view.StockChangeView;
import az.ibar.payday.ms.stock.model.view.StockView;
import az.ibar.payday.ms.stock.model.view.UserStockView;

import java.util.List;

public interface StockService {
    List<UserStockView> findUserStocks(Long userId);

    List<StockView> findAll();

    List<StockView> findAllPreferential();

    List<StockChangeView> getStockVolatility(Long id);

    List<StockView> calculatePreferential();
}
