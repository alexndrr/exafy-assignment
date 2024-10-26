package com.aleksandar.exafy.repository;

import com.aleksandar.exafy.data.entities.TaskPriority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskPriorityRepository extends JpaRepository<TaskPriority, Integer> {
}
