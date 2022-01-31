package az.ibar.payday.ms.transaction.config;

import az.ibar.payday.ms.transaction.client.StockClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {
        StockClient.class
})
public class FeignDefaultConfig {

}