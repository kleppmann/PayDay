package az.ibar.payday.ms.stock.scheduler;

import az.ibar.payday.ms.stock.logger.SafeLogger;
import az.ibar.payday.ms.stock.model.enums.Currency;
import az.ibar.payday.ms.stock.service.StockListService;
import az.ibar.payday.ms.stock.service.StockService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class StockListScheduler {

    private static final SafeLogger logger = SafeLogger.getLogger(StockListScheduler.class);

    private final StockListService stockListService;
    private final StockService stockService;

    public StockListScheduler(StockListService stockListService,
                              StockService stockService) {
        this.stockListService = stockListService;
        this.stockService = stockService;
    }

    @Scheduled(cron = "${stock.update.cron}")
    public void updateStocks() {
        logger.info("updateStocks scheduled job start");
        Arrays.stream(Currency.values()).forEach(stockListService::retrieveAvailableStocks);
        stockService.calculatePreferential();
        logger.info("updateStocks scheduled job end");
    }
}
