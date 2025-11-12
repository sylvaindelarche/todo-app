package me.sylvain.todo;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.sylvain.todo.controller.ListController;
import me.sylvain.todo.controller.TaskController;
import me.sylvain.todo.messaging.RabbitPublisher;
import me.sylvain.todo.persistence.entity.Task;
import me.sylvain.todo.persistence.entity.TaskDTO;
import me.sylvain.todo.persistence.entity.TodoList;
import me.sylvain.todo.persistence.entity.TodoListDTO;
import me.sylvain.todo.persistence.repository.ListRepository;
import me.sylvain.todo.persistence.repository.TaskRepository;

@WebMvcTest({ListController.class, TaskController.class})
@ActiveProfiles("test")
class TodoListControllersTest {
    final Task task = Task.builder()
        .id(1L)
        .name("Eggs")
        .completed(false)
        .build();
    final Task updatedTask = Task.builder()
        .id(1L)
        .name("Milk")
        .completed(false)
        .build();
    final Task completedTask = Task.builder()
        .id(1L)
        .name("Eggs")
        .completed(true)
        .build();
    final Task secondTask = Task.builder()
        .id(1L)
        .name("Eggs")
        .completed(false)
        .build();
    final TodoList todoList = TodoList.builder()
        .id(1L)
        .title("Groceries")
        .archived(false)
        .tasks(null)
        .build();
    final TodoList updatedList = TodoList.builder()
        .id(1L)
        .title("Website")
        .archived(false)
        .tasks(null)
        .build();
    final TodoList archivedList = TodoList.builder()
        .id(1L)
        .title("Groceries")
        .archived(true)
        .tasks(null)
        .build();
    final TodoList listWithTask = TodoList.builder()
        .id(1L)
        .title("Groceries")
        .archived(false)
        .tasks(new ArrayList<>(Arrays.asList(task)))
        .build();
    final TodoListDTO todoListDTO = TodoListDTO.builder()
        .title("Groceries")
        .build();
    final TodoListDTO updatedListDTO = TodoListDTO.builder()
        .title("Website")
        .build();
    final TaskDTO taskDTO = TaskDTO.builder()
        .name("Eggs")
        .build();
    final TaskDTO updatedTaskDTO = TaskDTO.builder()
        .name("Milk")
        .build();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ListRepository listRepository;

    @MockitoBean
    private TaskRepository taskRepository;

    @MockitoBean
    private RabbitPublisher rabbitPublisher;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        doNothing().when(rabbitPublisher).publish(any());
    }

    @Test
    void testGetAllListsReturnsEmptyJsonArray() throws Exception {
        when(listRepository.findAll()).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/lists"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("[]")));
    }

    @Test
    void testGetAllListsReturnsJsonArray() throws Exception {
        List<TodoList> todoLists = Collections.singletonList(todoList);
        when(listRepository.findAll()).thenReturn(todoLists);
        this.mockMvc.perform(get("/lists"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(mapper.writeValueAsString(todoLists))));
    }

    @Test
    void testCreateListReturnsOk() throws Exception {
        when(listRepository.save(any())).thenReturn(todoList);
        this.mockMvc.perform(post("/lists")
            .header("Content-Type", "application/json").content(mapper.writeValueAsString(todoListDTO)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(mapper.writeValueAsString(todoList))));
    }

    @Test
    void testUpdateListReturnsOk() throws Exception {
        when(listRepository.findById(1L)).thenReturn(Optional.of(todoList));
        when(listRepository.save(any())).thenReturn(updatedList);
        this.mockMvc.perform(patch("/lists/1")
            .header("Content-Type", "application/json").content(mapper.writeValueAsString(updatedListDTO)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(mapper.writeValueAsString(updatedList))));
    }

    @Test
    void testUpdateListWithNonExistentListReturnsNotFound() throws Exception {
        this.mockMvc.perform(patch("/lists/404")
            .header("Content-Type", "application/json").content(mapper.writeValueAsString(updatedListDTO)))
            .andExpect(status().isNotFound());
    }    

    @Test
    void testDeleteListReturnsOk() throws Exception {
        when(listRepository.findById(1L)).thenReturn(Optional.of(todoList));
        doNothing().when(listRepository).deleteById(1L);
        this.mockMvc.perform(delete("/lists/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteListWithNonExistentListReturnsNotFound() throws Exception {
        this.mockMvc.perform(delete("/lists/404"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testArchiveListReturnsListWithArchivedFieldTrue() throws Exception {
        when(listRepository.findById(1L)).thenReturn(Optional.of(todoList));
        when(listRepository.save(any())).thenReturn(archivedList);
        this.mockMvc.perform(post("/lists/1/archive"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(mapper.writeValueAsString(archivedList))));
    }

    @Test
    void testArchiveListWithNonExistentListReturnsNotFound() throws Exception {
        this.mockMvc.perform(post("/lists/404/archive"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testAddTaskReturnsOk() throws Exception {
        when(listRepository.findById(1L)).thenReturn(Optional.of(listWithTask));
        when(taskRepository.save(any())).thenReturn(task);
        this.mockMvc.perform(post("/lists/1/tasks")
            .header("Content-Type", "application/json").content(mapper.writeValueAsString(taskDTO)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(mapper.writeValueAsString(task))));
    }

    @Test
    void testAddTaskWithNonExistentListReturnsNotFound() throws Exception {
        this.mockMvc.perform(post("/lists/404/tasks")
            .header("Content-Type", "application/json").content(mapper.writeValueAsString(taskDTO)))
            .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateTaskReturnsOk() throws Exception {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any())).thenReturn(updatedTask);
        this.mockMvc.perform(patch("/tasks/1")
            .header("Content-Type", "application/json").content(mapper.writeValueAsString(updatedTaskDTO)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(mapper.writeValueAsString(updatedTask))));
    }

    @Test
    void testUpdateTaskWithNonExistentTaskReturnsNotFound() throws Exception {
        this.mockMvc.perform(patch("/tasks/404")
            .header("Content-Type", "application/json").content(mapper.writeValueAsString(updatedTaskDTO)))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteTaskReturnsOk() throws Exception {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).deleteById(1L);
        this.mockMvc.perform(delete("/tasks/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteTaskWithNonExistentTaskReturnsNotFound() throws Exception {
        this.mockMvc.perform(delete("/tasks/404"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testCompleteTaskReturnsTaskWithCompletedFieldTrue() throws Exception {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any())).thenReturn(completedTask);
        this.mockMvc.perform(post("/tasks/1/complete"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(mapper.writeValueAsString(completedTask))));
    }

    @Test
    void testCompleteTaskWithNonExistentTaskReturnsNotFound() throws Exception {
        this.mockMvc.perform(post("/tasks/404/complete"))
            .andExpect(status().isNotFound());
    }
}
