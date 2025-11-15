package me.sylvain.todo.shared;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import me.sylvain.todo.persistence.entity.Journal;

@Component
public class LatestJournalEntries {
    private List<Journal> list = new ArrayList<>();

    public void addEntry(Journal journal) {
        list.add(journal);
    }
    
    public List<Journal> getList() {
        return list;
    }

    public void clearList() {
        list.clear();
    }
}
