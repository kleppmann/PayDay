package az.ibar.payday.ms.transaction.config;

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
    private final String stockTransactionRequestQ;
    private final String stockTransactionRequestDLQ;
    private final String stockTransactionRequestQExchange;
    private final String stockTransactionRequestDLQExchange;
    private final String stockTransactionRequestQKey;
    private final String stockTransactionRequestDLQKey;

    public RabbitMQConfig(@Value("${rabbitmq.stockTransactionRequestQ}") String stockTransactionRequestQ,
                          @Value("${rabbitmq.stockTransactionRequestDLQ}") String stockTransactionRequestDLQ) {
        this.stockTransactionRequestQ = stockTransactionRequestQ;
        this.stockTransactionRequestDLQ = stockTransactionRequestDLQ;
        this.stockTransactionRequestQExchange = stockTransactionRequestQ + "_Exchange";
        this.stockTransactionRequestDLQExchange = stockTransactionRequestDLQ + "_Exchange";
        this.stockTransactionRequestQKey = stockTransactionRequestQ + "_Key";
        this.stockTransactionRequestDLQKey = stockTransactionRequestDLQ + "_Key";
    }

    @Bean
    Queue queue() {
        return QueueBuilder.durable(stockTransactionRequestQ)
                .withArgument("x-dead-letter-exchange", stockTransactionRequestDLQExchange)
                .withArgument("x-dead-letter-routing-key", stockTransactionRequestDLQKey)
                .build();
    }

    @Bean
    Queue dlq() {
        return QueueBuilder.durable(stockTransactionRequestDLQ).build();
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(stockTransactionRequestQExchange);
    }

    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange(stockTransactionRequestDLQExchange);
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(stockTransactionRequestQKey);
    }

    @Bean
    Binding dlqBinding() {
        return BindingBuilder.bind(dlq()).to(deadLetterExchange()).with(stockTransactionRequestDLQKey);
    }
}