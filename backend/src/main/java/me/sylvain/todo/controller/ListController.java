package me.sylvain.todo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import me.sylvain.todo.messaging.RabbitPublisher;
import me.sylvain.todo.persistence.entity.Task;
import me.sylvain.todo.persistence.entity.TaskDTO;
import me.sylvain.todo.persistence.entity.TodoList;
import me.sylvain.todo.persistence.entity.TodoListDTO;
import me.sylvain.todo.persistence.repository.ListRepository;
import me.sylvain.todo.persistence.repository.TaskRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/lists")
public class ListController {
    private final ListRepository listRepository;
    private final TaskRepository taskRepository;
    private final RabbitPublisher rabbitPublisher;

    public ListController(ListRepository listRepository, TaskRepository taskRepository, RabbitPublisher rabbitPublisher) {
        this.taskRepository = taskRepository;
        this.listRepository = listRepository;
        this.rabbitPublisher = rabbitPublisher;
    }
    
    @PostMapping
    public ResponseEntity<TodoList> createList(@Valid @RequestBody TodoListDTO listDTO) {
        TodoList list = new TodoList();
        if (listDTO.getTitle() != null) list.setTitle(listDTO.getTitle());
        list.setArchived(false);
        list.setTasks(new ArrayList<>());
        TodoList createdList = listRepository.save(list);
        rabbitPublisher.publish("Created list " + createdList.getTitle());
        return ResponseEntity.status(HttpStatus.OK).body(createdList);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<TodoList> updateList(@PathVariable Long id, @Valid @RequestBody TodoListDTO listDTO) {
        return listRepository.findById(id).map(list -> {
            if (listDTO.getTitle() != null) list.setTitle(listDTO.getTitle());
            return ResponseEntity.status(HttpStatus.OK).body(listRepository.save(list));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable Long id) {
        if (listRepository.findById(id).isPresent()) {
            listRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<List<TodoList>> getAllLists() {
        return ResponseEntity.status(HttpStatus.OK).body(listRepository.findAll());
    }
    
    @PostMapping("/{id}/archive")
    public ResponseEntity<TodoList> archiveList(@PathVariable Long id) {
        return listRepository.findById(id).map(list -> {
            list.setArchived(list.isArchived() ? false : true);
            return ResponseEntity.status(HttpStatus.OK).body(listRepository.save(list));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/{id}/tasks")
    public ResponseEntity<Task> addTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO) {

        Task task = new Task();
        if (taskDTO.getName() != null) task.setName(taskDTO.getName());
        task.setCompleted(false);

        TodoList list = listRepository.findById(id).orElse(null);
        if (list == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        list.getTasks().add(task);
        task.setList(list);
        return ResponseEntity.status(HttpStatus.OK).body(taskRepository.save(task));
    }
}
