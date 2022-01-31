package az.ibar.payday.ms.stock.client;

import az.ibar.payday.ms.stock.model.enums.Currency;
import az.ibar.payday.ms.stock.helper.StockListHelper;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockListClientFallbackFactory implements FallbackFactory<StockListClient> {

    private final StockListHelper stockListHelper;

    public StockListClientFallbackFactory(StockListHelper stockListHelper) {
        this.stockListHelper = stockListHelper;
    }

    @Override
    public StockListClient create(Throwable cause) {
        return new StockListClient() {
            @Override
            public List<StockDto> getDubaiStocks() {
                return stockListHelper.getStocksFromCache(Currency.AED);
            }

            @Override
            public List<StockDto> getMilanStocks() {
                return stockListHelper.getStocksFromCache(Currency.EUR);
            }

            @Override
            public List<StockDto> getNYStocks() {
                return stockListHelper.getStocksFromCache(Currency.USD);
            }
        };
    }
}
