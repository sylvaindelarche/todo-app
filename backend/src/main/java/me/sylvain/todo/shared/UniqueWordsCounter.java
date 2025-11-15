package me.sylvain.todo.shared;

import org.springframework.stereotype.Component;

@Component
public class UniqueWordsCounter {
    private int value = 0;

    public void setValue(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
}
