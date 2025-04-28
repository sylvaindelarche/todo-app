package me.sylvain.todo.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import me.sylvain.todo.persistence.entity.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {}
