package az.ibar.payday.ms.websocket.helper;

import az.ibar.payday.ms.websocket.exception.CacheException;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
public class CacheHelper {

    private static final String CACHE_KEY_FORMAT = "%s:%s";
    private static final String CACHE_WEBSOCKET_PREFIX = "web_socket";

    private final RedissonClient redissonClient;

    public CacheHelper(
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

    public void cacheUserSession(String key, String session) {
        RBucket<String> bucket = getBucket(key);
        bucket.set(session);
    }

    public void deleteUserSession(String key) {
        RBucket<String> bucket = getBucket(key);
        bucket.delete();
    }

    public String getWebsocketBucketKey(String username) {
        return String.format(CACHE_KEY_FORMAT, CACHE_WEBSOCKET_PREFIX, username);
    }
}
