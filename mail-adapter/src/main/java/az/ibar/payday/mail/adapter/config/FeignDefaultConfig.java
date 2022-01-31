package az.ibar.payday.mail.adapter.config;

import az.ibar.payday.mail.adapter.client.UserClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {
        UserClient.class
})
public class FeignDefaultConfig {

}