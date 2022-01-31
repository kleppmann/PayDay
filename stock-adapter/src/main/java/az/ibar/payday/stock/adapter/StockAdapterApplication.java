package az.ibar.payday.stock.adapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableEurekaClient
@EnableScheduling
@SpringBootApplication
public class StockAdapterApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockAdapterApplication.class, args);
    }
}

