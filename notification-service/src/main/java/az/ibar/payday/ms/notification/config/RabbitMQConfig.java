package az.ibar.payday.ms.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    private final String notificationQ;
    private final String notificationDLQ;
    private final String notificationQExchange;
    private final String notificationDLQExchange;
    private final String notificationQKey;
    private final String notificationDLQKey;

    public RabbitMQConfig(@Value("${rabbitmq.notificationQ}") String notificationQ,
                          @Value("${rabbitmq.notificationDLQ}") String notificationDLQ) {
        this.notificationQ = notificationQ;
        this.notificationDLQ = notificationDLQ;
        this.notificationQExchange = notificationQ + "_Exchange";
        this.notificationDLQExchange = notificationDLQ + "_Exchange";
        this.notificationQKey = notificationQ + "_Key";
        this.notificationDLQKey = notificationDLQ + "_Key";
    }

    @Bean
    Queue queue() {
        return QueueBuilder.durable(notificationQ)
                .withArgument("x-dead-letter-exchange", notificationDLQExchange)
                .withArgument("x-dead-letter-routing-key", notificationDLQKey)
                .build();
    }

    @Bean
    Queue dlq() {
        return QueueBuilder.durable(notificationDLQ).build();
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(notificationQExchange);
    }

    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange(notificationDLQExchange);
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(notificationQKey);
    }

    @Bean
    Binding dlqBinding() {
        return BindingBuilder.bind(dlq()).to(deadLetterExchange()).with(notificationDLQKey);
    }
}