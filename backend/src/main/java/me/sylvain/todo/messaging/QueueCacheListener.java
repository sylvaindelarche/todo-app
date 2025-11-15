package me.sylvain.todo.messaging;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.sylvain.todo.persistence.entity.Journal;
import me.sylvain.todo.shared.LatestJournalEntries;

@Component
public class QueueCacheListener {

	@Autowired
	private LatestJournalEntries latestJournalEntries;

	@RabbitListener(queues = RabbitConfig.CACHE_QUEUE_NAME)
	public void receiveMessage(String content) {
		Journal journal = new Journal();
		journal.setHtml(content);
		journal.setCreatedAt(LocalDateTime.now());
		latestJournalEntries.addEntry(journal);
	}
}
