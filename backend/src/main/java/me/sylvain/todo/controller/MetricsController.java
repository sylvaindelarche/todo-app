package me.sylvain.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.sylvain.todo.persistence.cache.UniqueWordsCount;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/metrics")
public class MetricsController {
    @Autowired
    private UniqueWordsCount uniqueWordsCount;

    @GetMapping
    public ResponseEntity<Integer> get() {
        return ResponseEntity.status(HttpStatus.OK).body(uniqueWordsCount.getCache());
    }
}
