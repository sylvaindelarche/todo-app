package me.sylvain.todo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import me.sylvain.todo.persistence.entity.TodoList;
import me.sylvain.todo.persistence.entity.TodoListDTO;
import me.sylvain.todo.persistence.repository.ListRepository;

@RestController
@RequestMapping("/lists")
public class ListController {
    private final ListRepository listRepository;

    public ListController(ListRepository listRepository) {
        this.listRepository = listRepository;
    }
    
    @PostMapping
    public ResponseEntity<TodoList> createList(@Valid @RequestBody TodoListDTO listDTO) {
        TodoList list = new TodoList();
        list.setTitle(listDTO.getTitle());

        return ResponseEntity.ok(listRepository.save(list));
    }

}
