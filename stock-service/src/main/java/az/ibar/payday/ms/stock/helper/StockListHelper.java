package az.ibar.payday.ms.stock.helper;

import az.ibar.payday.ms.stock.client.StockDto;
import az.ibar.payday.ms.stock.logger.SafeLogger;
import az.ibar.payday.ms.stock.mapper.StockMapper;
import az.ibar.payday.ms.stock.model.enums.Currency;
import az.ibar.payday.ms.stock.repository.StockRepository;
import org.redisson.api.RBucket;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockListHelper {

    private static final SafeLogger logger = SafeLogger.getLogger(StockListHelper.class);

    private final CacheHelper cacheHelper;
    private final StockRepository stockRepository;

    public StockListHelper(CacheHelper cacheHelper, StockRepository stockRepository) {
        this.cacheHelper = cacheHelper;
        this.stockRepository = stockRepository;
    }

    public List<StockDto> getStocksFromCache(Currency currency) {
        logger.info("getStocksFromCache start for {}", currency);
        RBucket<List<StockDto>> stockBucket = cacheHelper
                .getBucket(cacheHelper.getStockBucketKey(currency.name()));
        logger.info("getStocksFromCache end for {}", currency);
        return stockBucket.get() == null ? getStocksFromDB(currency) : stockBucket.get();
    }

    private List<StockDto> getStocksFromDB(Currency currency) {
        logger.info("getStocksFromDB start for {}", currency);
        var stockEntities = stockRepository.findAllByCurrency(currency);
        logger.info("getStocksFromDB end for {}", currency);
        return stockEntities
                .stream()
                .map(StockMapper.INSTANCE::stockDtoFromEntity)
                .collect(Collectors.toList());
    }
}
