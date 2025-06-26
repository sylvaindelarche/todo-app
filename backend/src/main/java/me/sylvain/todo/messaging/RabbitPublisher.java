package me.sylvain.todo.messaging;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitPublisher {

    private final AmqpTemplate amqpTemplate;

    @Autowired
    public RabbitPublisher(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void publish(String message) {
        amqpTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, "", message);
    }
}