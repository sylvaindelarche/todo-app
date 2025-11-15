package me.sylvain.todo.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.sylvain.todo.shared.UniqueWordsCounter;

@Component
public class QueueUniqueWordsListener {

	@Autowired
	private UniqueWordsCounter counter;

	@RabbitListener(queues = RabbitConfig.UNIQUE_WORDS_QUEUE_NAME)
	public void receiveMessage(String content) {
		counter.setValue(Integer.parseInt(content));
	}
}
