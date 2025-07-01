package me.sylvain.todo.persistence.cache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import me.sylvain.todo.persistence.entity.Journal;

@Component
public class JournalCache {
    private List<Journal> cache = new ArrayList<>();

    public void addEntry(Journal journal) {
        cache.add(journal);
    }
    
    public List<Journal> getCache() {
        return cache;
    }

    public void clearCache() {
        cache.clear();
    }
}
