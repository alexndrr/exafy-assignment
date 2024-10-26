package com.aleksandar.exafy.repository;

import com.aleksandar.exafy.data.entities.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Integer> {
    Optional<TaskStatus> findByName(String name);
}
