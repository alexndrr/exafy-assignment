package com.aleksandar.exafy.repository;

import com.aleksandar.exafy.data.entities.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Integer> {
}
