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

import me.sylvain.todo.persistence.entity.Journal;
import me.sylvain.todo.persistence.repository.JournalRepository;
import me.sylvain.todo.shared.JournalEntries;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/journals")
public class JournalController {
    private final JournalRepository journalRepository;
    
    @Autowired
    private JournalEntries journalEntries;

    public JournalController(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @GetMapping
    public ResponseEntity<List<Journal>> getAllEntries() {
        List<Journal> wholeJournalList = new ArrayList<>(journalRepository.findAll());
        journalEntries.clearList();
        return ResponseEntity.status(HttpStatus.OK).body(wholeJournalList);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Journal>> getLatestEntries() {
        List<Journal> latestEntriesList = new ArrayList<>(journalEntries.getList());
        journalEntries.clearList();
        return ResponseEntity.status(HttpStatus.OK).body(latestEntriesList);
    }
}
