package az.ibar.payday.ms.stock.controller;

import az.ibar.payday.ms.stock.model.StockTransactionRequest;
import az.ibar.payday.ms.stock.model.StockTransactionResponse;
import az.ibar.payday.ms.stock.model.view.StockChangeView;
import az.ibar.payday.ms.stock.model.view.StockView;
import az.ibar.payday.ms.stock.model.view.UserStockView;
import az.ibar.payday.ms.stock.service.StockListService;
import az.ibar.payday.ms.stock.service.StockService;
import az.ibar.payday.ms.stock.service.StockTransactionService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class StockController {

    private final StockService stockService;
    private final StockTransactionService stockTransactionService;

    public StockController(StockService stockService,
                           StockTransactionService stockTransactionService) {
        this.stockService = stockService;
        this.stockTransactionService = stockTransactionService;
    }

    @PostMapping("/transaction")
    public void transaction(@RequestBody @Valid StockTransactionRequest transactionRequest) {
        stockTransactionService.executeTransaction(transactionRequest);
    }

    @PostMapping("/transaction/result")
    public void transactionResult(@RequestBody @Valid StockTransactionResponse transactionResponse) {
        stockTransactionService.executeTransactionResult(transactionResponse);
    }

    @GetMapping
    public List<StockView> getStocks() {
        return stockService.findAll();
    }

    @GetMapping("/preferential")
    public List<StockView> getPreferentialStocks() {
        return stockService.findAllPreferential();
    }

    @GetMapping("/{id}/volatility")
    public List<StockChangeView> getStockVolatility(@PathVariable Long id) {
        return stockService.getStockVolatility(id);
    }

    @GetMapping("/users/{userId}/stocks")
    public List<UserStockView> getUserStocks(@PathVariable("userId") Long userId) {
        return stockService.findUserStocks(userId);
    }
}
