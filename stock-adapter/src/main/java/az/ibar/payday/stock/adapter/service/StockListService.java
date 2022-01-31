package az.ibar.payday.stock.adapter.service;

import az.ibar.payday.stock.adapter.model.StockDto;

import java.util.List;

public interface StockListService {

    List<StockDto> findAvailableDubaiStocks();

    List<StockDto> findAvailableMilanStocks();

    List<StockDto> findAvailableNYStocks();
}
