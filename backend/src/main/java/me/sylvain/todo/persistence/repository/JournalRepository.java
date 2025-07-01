package me.sylvain.todo.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.sylvain.todo.persistence.entity.Journal;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {}
