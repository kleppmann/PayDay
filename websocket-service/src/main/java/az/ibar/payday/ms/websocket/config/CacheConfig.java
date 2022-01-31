package az.ibar.payday.ms.websocket.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.SerializationCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CacheConfig {

    @Value("${redis.server.urls}")
    String[] redisServers;

    @Bean
    RedissonClient redissonSingleClient() {
        Config config = new Config();

        config.setCodec(new SerializationCodec())
                .useSingleServer()
                .setAddress(redisServers[0]);

        return Redisson.create(config);
    }
}
