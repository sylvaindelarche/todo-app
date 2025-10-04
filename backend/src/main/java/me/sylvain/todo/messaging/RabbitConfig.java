package me.sylvain.todo.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RabbitConfig {
    static final String EXCHANGE_NAME = "app-exchange";
    static final String DB_QUEUE_NAME = "queue.db";
    static final String CACHE_QUEUE_NAME = "queue.cache";
    static final String UNIQUE_WORDS_QUEUE_NAME = "queue.unique_words";

    @Bean
    FanoutExchange exchange() {
        return new FanoutExchange(EXCHANGE_NAME);
    }

    @Bean
    Queue dbQueue() {
        return new Queue(DB_QUEUE_NAME, false);
    }

    @Bean
    Queue cacheQueue() {
        return new Queue(CACHE_QUEUE_NAME, false);
    }

    @Bean
    Queue uniqueWordsQueue() {
        return new Queue(UNIQUE_WORDS_QUEUE_NAME, false);
    }

    @Bean
    Binding binding1(Queue dbQueue, FanoutExchange exchange) {
        return BindingBuilder.bind(dbQueue).to(exchange);
    }

    @Bean
    Binding binding2(Queue cacheQueue, FanoutExchange exchange) {
        return BindingBuilder.bind(cacheQueue).to(exchange);
    }

    @Bean
    Binding binding3(Queue uniqueWordsQueue, FanoutExchange exchange) {
        return BindingBuilder.bind(uniqueWordsQueue).to(exchange);
    }
}
