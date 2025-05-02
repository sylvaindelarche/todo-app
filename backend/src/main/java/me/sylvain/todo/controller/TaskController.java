package me.sylvain.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import me.sylvain.todo.persistence.entity.Task;
import me.sylvain.todo.persistence.entity.TaskDTO;
import me.sylvain.todo.persistence.repository.TaskRepository;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO) {
        return taskRepository.findById(id).map(task -> {
            if (taskDTO.getName() != null) task.setName(taskDTO.getName());
            return ResponseEntity.status(HttpStatus.OK).body(taskRepository.save(task));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskRepository.findById(id).isPresent()) {
            taskRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    
    @PostMapping("/{id}/complete")
    public ResponseEntity<Task> completeList(@PathVariable Long id) {
        return taskRepository.findById(id).map(task -> {
            task.setCompleted(task.isCompleted() ? false : true);
            return ResponseEntity.status(HttpStatus.OK).body(taskRepository.save(task));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
