package me.sylvain.todo.persistence.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import me.sylvain.todo.persistence.entity.Task;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {}
