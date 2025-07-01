package me.sylvain.todo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.sylvain.todo.persistence.cache.JournalCache;
import me.sylvain.todo.persistence.entity.Journal;
import me.sylvain.todo.persistence.repository.JournalRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/journal")
public class JournalController {
    private final JournalRepository journalRepository;
    
    @Autowired
    private JournalCache journalCache;

    public JournalController(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @GetMapping
    public ResponseEntity<List<Journal>> getAllEntries() {
        List<Journal> allJournals = new ArrayList<>(journalRepository.findAll());
        journalCache.clearCache();
        return ResponseEntity.status(HttpStatus.OK).body(allJournals);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Journal>> getLatestEntries() {
        List<Journal> latestJournals = new ArrayList<>(journalCache.getCache());
        journalCache.clearCache();
        return ResponseEntity.status(HttpStatus.OK).body(latestJournals);
    }
}
