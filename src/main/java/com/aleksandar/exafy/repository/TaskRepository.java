package com.aleksandar.exafy.repository;

import com.aleksandar.exafy.data.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Query("SELECT t FROM Task t WHERE t.status.id = :statusId")
    Page<Task> findAllByStatusId(@Param("statusId") Integer statusId, Pageable pageable);
    @Query("SELECT t FROM Task t WHERE t.dueDate > :currentDate")
    List<Task> findAllActiveTasks(@Param("currentDate")LocalDateTime currentDate);
}
