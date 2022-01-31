package az.ibar.payday.ms.stock.service;

import az.ibar.payday.ms.stock.client.StockDto;
import az.ibar.payday.ms.stock.model.enums.Currency;

import java.util.List;

public interface StockListService {
    List<StockDto> retrieveAvailableStocks(Currency currency);
}
