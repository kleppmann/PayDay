package az.ibar.payday.ms.stock.helper.impl;

import az.ibar.payday.ms.stock.client.StockDto;
import az.ibar.payday.ms.stock.exception.CacheException;
import az.ibar.payday.ms.stock.helper.CacheHelper;
import az.ibar.payday.ms.stock.model.view.StockView;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("default")
@Primary
@Component
public class CacheHelperImpl implements CacheHelper {

    private static final String CACHE_KEY_FORMAT = "%s:%s";
    private static final String CACHE_STOCK_PREFIX = "stock";
    private static final String CACHE_PREFERENTIAL_STOCK_PREFIX = "preferential_stock";

    private final RedissonClient redissonClient;

    public CacheHelperImpl(
            RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public <T> RBucket<T> getBucket(String cacheKey) {
        RBucket<T> bucket = redissonClient.getBucket(cacheKey);
        if (bucket == null) {
            throw new CacheException("bucket is null");
        }
        return bucket;
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
