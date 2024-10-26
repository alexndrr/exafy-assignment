package com.aleksandar.exafy.repository;

import com.aleksandar.exafy.data.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TaskRepositoryTest {

    @Mock
    private TaskRepository taskRepository;

    private User existingUser;
    private TaskCategory existingCategory;
    private TaskPriority existingPriority;
    private TaskStatus existingStatus;
    private Task existingTask, existingTaskPastDueDate;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        existingUser = User
                .builder()
                .id(1)
                .email("sendemail838@gmail.com")
                .build();

        existingCategory = TaskCategory
                .builder()
                .id(1)
                .name("Work")
                .build();

        existingPriority = TaskPriority
                .builder()
                .id(1)
                .name("High")
                .build();

        existingStatus = TaskStatus
                .builder()
                .id(1)
                .name("In Progress")
                .build();

        existingTask = Task
                .builder()
                .id(1)
                .title("Title")
                .description("Description")
                .dueDate(LocalDateTime.now().plusDays(1))
                .category(existingCategory)
                .priority(existingPriority)
                .status(existingStatus)
                .user(existingUser)
                .build();

        existingTaskPastDueDate = Task
                .builder()
                .id(1)
                .title("Title")
                .description("Description")
                .dueDate(LocalDateTime.now().minusDays(1))
                .category(existingCategory)
                .priority(existingPriority)
                .status(existingStatus)
                .user(existingUser)
                .build();
    }

    @Test
    void shouldReturnActiveTask()
    {
        List<Task> expectedTasks = List.of(existingTask);

        when(taskRepository.findAllActiveTasks(any(LocalDateTime.class))).thenReturn(expectedTasks);

        var response = taskRepository.findAllActiveTasks(LocalDateTime.now());

        assertEquals(1, response.size());
        assertEquals(expectedTasks, response);
    }
}
