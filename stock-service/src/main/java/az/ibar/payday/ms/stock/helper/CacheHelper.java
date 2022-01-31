package az.ibar.payday.ms.stock.helper;

import az.ibar.payday.ms.stock.client.StockDto;
import az.ibar.payday.ms.stock.model.view.StockView;
import org.redisson.api.RBucket;

import java.util.List;

public interface CacheHelper {

    <T> RBucket<T> getBucket(String cacheKey);

    void cacheStockDtos(String key, List<StockDto> stockDtos);

    void cacheStockViews(String key, List<StockView> stockViews);

    String getStockBucketKey(String stockKey);

    String getPreferentialStockBucketKey(String stockKey);
}
