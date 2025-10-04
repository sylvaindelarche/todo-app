package me.sylvain.todo.persistence.cache;

import org.springframework.stereotype.Component;

@Component
public class UniqueWordsCount {
    private int cache = 0;

    public void setCache(int value) {
        cache = value;
    }
    
    public int getCache() {
        return cache;
    }
}
