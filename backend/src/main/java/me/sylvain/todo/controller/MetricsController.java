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

    @GetMapping(produces = "text/plain; version=0.0.4")
    public ResponseEntity<String> get() {
        String s = "# HELP unique_words Unique words\n";
        String s2 = "# TYPE unique_words gauge\n";
        String s3 = "unique_words " + uniqueWordsCount.getCache() + "\n";
        return ResponseEntity.status(HttpStatus.OK).body(s + s2 + s3);
    }
}
