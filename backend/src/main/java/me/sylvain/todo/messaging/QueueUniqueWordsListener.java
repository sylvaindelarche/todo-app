package me.sylvain.todo.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.sylvain.todo.persistence.cache.UniqueWordsCount;

@Component
public class QueueUniqueWordsListener {

	@Autowired
	private UniqueWordsCount cache;

	@RabbitListener(queues = RabbitConfig.UNIQUE_WORDS_QUEUE_NAME)
	public void receiveMessage(String content) {
		cache.setCache(Integer.parseInt(content));
	}
}
