package az.ibar.payday.mail.adapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MailAdapterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailAdapterApplication.class, args);
    }
}
