package az.ibar.payday.ms.websocket.config;

import az.ibar.payday.ms.websocket.client.UserClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {
        UserClient.class
})
public class FeignDefaultConfig {

}