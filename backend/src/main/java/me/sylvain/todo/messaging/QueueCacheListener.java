package me.sylvain.todo.messaging;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.sylvain.todo.persistence.cache.JournalCache;
import me.sylvain.todo.persistence.entity.Journal;

@Component
public class QueueCacheListener {

	@Autowired
	private JournalCache journalCache;

	@RabbitListener(queues = RabbitConfig.CACHE_QUEUE_NAME)
	public void receiveMessage(String content) {
		Journal journal = new Journal();
		journal.setHtml(content);
		journal.setCreatedAt(LocalDateTime.now());
		journalCache.addEntry(journal);
	}
}
