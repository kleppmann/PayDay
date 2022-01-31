package az.ibar.payday.mail.adapter.config;

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
    private final String emailQ;
    private final String emailDLQ;
    private final String emailQExchange;
    private final String emailDLQExchange;
    private final String emailQKey;
    private final String emailDLQKey;

    public RabbitMQConfig(@Value("${rabbitmq.emailQ}") String emailQ,
                          @Value("${rabbitmq.emailDLQ}") String emailDLQ) {
        this.emailQ = emailQ;
        this.emailDLQ = emailDLQ;
        this.emailQExchange = emailQ + "_Exchange";
        this.emailDLQExchange = emailDLQ + "_Exchange";
        this.emailQKey = emailQ + "_Key";
        this.emailDLQKey = emailDLQ + "_Key";
    }

    @Bean
    Queue queue() {
        return QueueBuilder.durable(emailQ)
                .withArgument("x-dead-letter-exchange", emailDLQExchange)
                .withArgument("x-dead-letter-routing-key", emailDLQKey)
                .build();
    }

    @Bean
    Queue dlq() {
        return QueueBuilder.durable(emailDLQ).build();
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(emailQExchange);
    }

    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange(emailDLQExchange);
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(emailQKey);
    }

    @Bean
    Binding dlqBinding() {
        return BindingBuilder.bind(dlq()).to(deadLetterExchange()).with(emailDLQKey);
    }
}