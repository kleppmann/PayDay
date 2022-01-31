package az.ibar.payday.ms.stock.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Primary
@FeignClient(value = "stock-adapter", fallbackFactory = StockListClientFallbackFactory.class)
public interface StockListClient {

    @GetMapping(value = "/stocks/dubai")
    List<StockDto> getDubaiStocks();

    @GetMapping(value = "/stocks/milan")
    List<StockDto> getMilanStocks();

    @GetMapping(value = "/stocks/ny")
    List<StockDto> getNYStocks();
}
