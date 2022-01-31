package az.ibar.payday.ms.stock.config;

import az.ibar.payday.ms.stock.client.StockListClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {
        StockListClient.class
})
public class FeignDefaultConfig {

    @Bean
    public FeignTraceRequestInterceptor feignTraceRequestInterceptor() {
        return new FeignTraceRequestInterceptor();
    }
}