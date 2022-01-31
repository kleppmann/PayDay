package az.ibar.payday.ms.stock.helper.mock;

import az.ibar.payday.ms.stock.client.StockDto;
import az.ibar.payday.ms.stock.exception.CacheException;
import az.ibar.payday.ms.stock.helper.CacheHelper;
import az.ibar.payday.ms.stock.model.view.StockView;
import org.redisson.api.RBucket;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("test")
@Component
public class CacheHelperMock implements CacheHelper {

    private static final String CACHE_KEY_FORMAT = "%s:%s";
    private static final String CACHE_STOCK_PREFIX = "stock";
    private static final String CACHE_PREFERENTIAL_STOCK_PREFIX = "preferential_stock";

    public <T> RBucket<T> getBucket(String cacheKey) {
        throw new CacheException("bucket is null");
    }

    public void cacheStockDtos(String key, List<StockDto> stockDtos) {
        RBucket<List<StockDto>> bucket = getBucket(key);
        bucket.set(stockDtos);
    }

    public void cacheStockViews(String key, List<StockView> stockViews) {
        RBucket<List<StockView>> bucket = getBucket(key);
        bucket.set(stockViews);
    }

    public String getStockBucketKey(String stockKey) {
        return String.format(CACHE_KEY_FORMAT, CACHE_STOCK_PREFIX, stockKey);
    }

    public String getPreferentialStockBucketKey(String stockKey) {
        return String.format(CACHE_KEY_FORMAT, CACHE_PREFERENTIAL_STOCK_PREFIX, stockKey);
    }
}
