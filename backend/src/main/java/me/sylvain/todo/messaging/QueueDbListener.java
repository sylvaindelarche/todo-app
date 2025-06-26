package me.sylvain.todo.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class QueueDbListener {
	@RabbitListener(queues = RabbitConfig.QUEUE_NAME)
	public void receiveMessage(String content) {
        System.out.println("Received <<" + content + ">>");
	}
}
