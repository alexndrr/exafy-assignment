package com.aleksandar.exafy.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private LocalDateTime dueDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_priority_id")
    private TaskPriority priority;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_status_id")
    private TaskStatus status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_category_id")
    private TaskCategory category;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "app_user_id")
    private User user;
}
