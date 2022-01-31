package az.ibar.payday.ms.websocket.config;

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
    private final String websocketRequestQ;
    private final String websocketRequestDLQ;
    private final String websocketRequestQExchange;
    private final String websocketRequestDLQExchange;
    private final String websocketRequestQKey;
    private final String websocketRequestDLQKey;
    private final String websocketResponseQ;
    private final String websocketResponseDLQ;
    private final String websocketResponseQExchange;
    private final String websocketResponseDLQExchange;
    private final String websocketResponseQKey;
    private final String websocketResponseDLQKey;

    public RabbitMQConfig(@Value("${rabbitmq.websocketRequestQ}") String websocketRequestQ,
                          @Value("${rabbitmq.websocketRequestDLQ}") String websocketRequestDLQ,
                          @Value("${rabbitmq.websocketResponseQ}") String websocketResponseQ,
                          @Value("${rabbitmq.websocketResponseDLQ}") String websocketResponseDLQ) {
        this.websocketRequestQ = websocketRequestQ;
        this.websocketRequestDLQ = websocketRequestDLQ;
        this.websocketRequestQExchange = websocketRequestQ + "_Exchange";
        this.websocketRequestDLQExchange = websocketRequestDLQ + "_Exchange";
        this.websocketRequestQKey = websocketRequestQ + "_Key";
        this.websocketRequestDLQKey = websocketRequestDLQ + "_Key";
        this.websocketResponseQ = websocketResponseQ;
        this.websocketResponseDLQ = websocketResponseDLQ;
        this.websocketResponseQExchange = websocketResponseQ + "_Exchange";
        this.websocketResponseDLQExchange = websocketResponseDLQ + "_Exchange";
        this.websocketResponseQKey = websocketResponseQ + "_Key";
        this.websocketResponseDLQKey = websocketResponseDLQ + "_Key";
    }

    @Bean
    Queue queue() {
        return QueueBuilder.durable(websocketRequestQ)
                .withArgument("x-dead-letter-exchange", websocketRequestDLQExchange)
                .withArgument("x-dead-letter-routing-key", websocketRequestDLQKey)
                .build();
    }

    @Bean
    Queue dlq() {
        return QueueBuilder.durable(websocketRequestDLQ).build();
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(websocketRequestQExchange);
    }

    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange(websocketRequestDLQExchange);
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(websocketRequestQKey);
    }

    @Bean
    Binding dlqBinding() {
        return BindingBuilder.bind(dlq()).to(deadLetterExchange()).with(websocketRequestDLQKey);
    }

    @Bean
    Queue responseQueue() {
        return QueueBuilder.durable(websocketResponseQ)
                .withArgument("x-dead-letter-exchange", websocketResponseDLQExchange)
                .withArgument("x-dead-letter-routing-key", websocketResponseDLQKey)
                .build();
    }

    @Bean
    Queue responseDlq() {
        return QueueBuilder.durable(websocketResponseDLQ).build();
    }

    @Bean
    DirectExchange responseExchange() {
        return new DirectExchange(websocketResponseQExchange);
    }

    @Bean
    DirectExchange responseDeadLetterExchange() {
        return new DirectExchange(websocketResponseDLQExchange);
    }

    @Bean
    Binding responseBinding() {
        return BindingBuilder.bind(responseQueue()).to(responseExchange()).with(websocketResponseQKey);
    }

    @Bean
    Binding responseDlqBinding() {
        return BindingBuilder.bind(responseDlq()).to(responseDeadLetterExchange()).with(websocketResponseDLQKey);
    }
}