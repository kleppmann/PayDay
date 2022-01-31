package az.ibar.payday.stock.adapter.controller;

import az.ibar.payday.stock.adapter.model.StockDto;
import az.ibar.payday.stock.adapter.service.StockListService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class StockController {

    private final StockListService stockListService;

    public StockController(StockListService stockListService) {
        this.stockListService = stockListService;
    }

    @GetMapping("/stocks/dubai")
    public List<StockDto> stocksDubai() {
        return stockListService.findAvailableDubaiStocks();
    }

    @GetMapping("/stocks/milan")
    public List<StockDto> stocksMilan() {
        return stockListService.findAvailableMilanStocks();
    }

    @GetMapping("/stocks/ny")
    public List<StockDto> stocksNY() {
        return stockListService.findAvailableNYStocks();
    }
}
