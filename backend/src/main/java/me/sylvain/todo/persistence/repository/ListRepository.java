package me.sylvain.todo.persistence.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import me.sylvain.todo.persistence.entity.TodoList;
import org.springframework.stereotype.Repository;


@Repository
public interface ListRepository extends JpaRepository<TodoList, Long> {}
