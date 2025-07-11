package me.sylvain.todo.messaging;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.sylvain.todo.persistence.entity.Journal;
import me.sylvain.todo.persistence.repository.JournalRepository;

@Component
public class QueueDbListener {

	@Autowired
	private JournalRepository journalRepository;

	@RabbitListener(queues = RabbitConfig.DB_QUEUE_NAME)
	public void receiveMessage(String content) {
		Journal journal = new Journal();
		journal.setHtml(content);
		journal.setCreatedAt(LocalDateTime.now());
		journalRepository.save(journal);
	}
}
