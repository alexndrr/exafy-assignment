package com.aleksandar.exafy.service;

import com.aleksandar.exafy.data.dtos.TaskRequestDto;
import com.aleksandar.exafy.data.entities.*;
import com.aleksandar.exafy.mapper.TaskMapper;
import com.aleksandar.exafy.repository.*;
import com.aleksandar.exafy.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskServiceTest {

    @InjectMocks
    private TaskServiceImpl taskServiceImpl;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskCategoryRepository taskCategoryRepository;

    @Mock
    private TaskPriorityRepository taskPriorityRepository;

    @Mock
    private TaskStatusRepository taskStatusRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    private User existingUser;
    private TaskCategory existingCategory;
    private TaskPriority existingPriority;
    private TaskStatus existingStatus;
    private Task existingTask;

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
    }

    @Test
    void taskCreateShouldFailMissingTitle()
    {
        TaskRequestDto request = new TaskRequestDto();
        request.setTitle("");

        assertThrows(IllegalArgumentException.class, () -> taskServiceImpl.create(request));
    }

    @Test
    void taskCreateShouldFailInvalidDueDate()
    {
        TaskRequestDto request = new TaskRequestDto();
        request.setTitle("Title");
        request.setDueDate(LocalDateTime.now());

        assertThrows(IllegalArgumentException.class, () -> taskServiceImpl.create(request));
    }

    @Test
    void taskCreateShouldFailUserNotFound()
    {
        TaskRequestDto request = new TaskRequestDto();
        request.setTitle("Title");
        request.setDueDate(LocalDateTime.now().plusDays(1));
        request.setUserId(1);

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskServiceImpl.create(request));
    }

    @Test
    void taskCreateShouldFailCategoryNotFound()
    {
        TaskRequestDto request = new TaskRequestDto();
        request.setTitle("Title");
        request.setDueDate(LocalDateTime.now().plusDays(1));
        request.setUserId(1);
        request.setCategoryId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(taskCategoryRepository.findById(1)).thenReturn(Optional.empty());

        assertNotNull(existingUser);
        assertThrows(NoSuchElementException.class, () -> taskServiceImpl.create(request));
    }

    @Test
    void taskCreateShouldFailPriorityNotFound()
    {
        TaskRequestDto request = new TaskRequestDto();
        request.setTitle("Title");
        request.setDueDate(LocalDateTime.now().plusDays(1));
        request.setUserId(1);
        request.setCategoryId(1);
        request.setPriorityId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(taskCategoryRepository.findById(1)).thenReturn(Optional.of(existingCategory));
        when(taskPriorityRepository.findById(1)).thenReturn(Optional.empty());

        assertNotNull(existingUser);
        assertNotNull(existingCategory);
        assertThrows(NoSuchElementException.class, () -> taskServiceImpl.create(request));
    }

    @Test
    void taskCreateShouldFailStatusNotFound()
    {
        TaskRequestDto request = new TaskRequestDto();
        request.setTitle("Title");
        request.setDueDate(LocalDateTime.now().plusDays(1));
        request.setUserId(1);
        request.setCategoryId(1);
        request.setPriorityId(1);
        request.setStatusId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(taskCategoryRepository.findById(1)).thenReturn(Optional.of(existingCategory));
        when(taskPriorityRepository.findById(1)).thenReturn(Optional.of(existingPriority));
        when(taskStatusRepository.findById(1)).thenReturn(Optional.empty());

        assertNotNull(existingUser);
        assertNotNull(existingCategory);
        assertNotNull(existingPriority);
        assertThrows(NoSuchElementException.class, () -> taskServiceImpl.create(request));
    }

    @Test
    void shouldCreateTask()
    {
        var request = TaskRequestDto
                .builder()
                .title("Title")
                .description("Description")
                .dueDate(LocalDateTime.now().plusDays(1))
                .categoryId(1)
                .priorityId(1)
                .statusId(1)
                .userId(1)
                .build();

        var newTask = Task
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

        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(taskCategoryRepository.findById(1)).thenReturn(Optional.of(existingCategory));
        when(taskPriorityRepository.findById(1)).thenReturn(Optional.of(existingPriority));
        when(taskStatusRepository.findById(1)).thenReturn(Optional.of(existingStatus));
        when(taskRepository.save(any(Task.class))).thenReturn(newTask);

        var response = taskServiceImpl.create(request);

        assertNotNull(response);
        verify(taskRepository).save(any(Task.class));
        verify(emailService).sendNewTaskEmail(existingUser.getEmail(), newTask);

        assertEquals(TaskMapper.mapToDto(newTask), response);
    }

    @Test
    void taskUpdateShouldFailMissingTitle()
    {
        TaskRequestDto request = new TaskRequestDto();
        request.setTitle("");

        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));

        assertNotNull(existingTask);
        assertThrows(IllegalArgumentException.class, () -> taskServiceImpl.create(request));
    }

    @Test
    void taskUpdateShouldFailInvalidDueDate()
    {
        TaskRequestDto request = new TaskRequestDto();
        request.setTitle("Title");
        request.setDueDate(LocalDateTime.now());

        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));

        assertNotNull(existingTask);
        assertThrows(IllegalArgumentException.class, () -> taskServiceImpl.create(request));
    }

    @Test
    void taskUpdateShouldFailUserNotFound()
    {
        TaskRequestDto request = new TaskRequestDto();
        request.setTitle("Title");
        request.setDueDate(LocalDateTime.now().plusDays(1));
        request.setUserId(1);

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskServiceImpl.create(request));
    }

    @Test
    void taskUpdateShouldFailCategoryNotFound()
    {
        TaskRequestDto request = new TaskRequestDto();
        request.setTitle("Title");
        request.setDueDate(LocalDateTime.now().plusDays(1));
        request.setCategoryId(1);

        when(taskCategoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskServiceImpl.create(request));
    }

    @Test
    void taskUpdateShouldFailPriorityNotFound()
    {
        TaskRequestDto request = new TaskRequestDto();
        request.setTitle("Title");
        request.setDueDate(LocalDateTime.now().plusDays(1));
        request.setPriorityId(1);

        when(taskPriorityRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskServiceImpl.create(request));
    }

    @Test
    void taskUpdateShouldFailStatusNotFound()
    {
        TaskRequestDto request = new TaskRequestDto();
        request.setTitle("Title");
        request.setDueDate(LocalDateTime.now().plusDays(1));
        request.setStatusId(1);

        when(taskStatusRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskServiceImpl.create(request));
    }

    @Test
    void shouldUpdateTaskNotReassigned()
    {
        var request = TaskRequestDto
                .builder()
                .title("Title")
                .description("Description")
                .dueDate(LocalDateTime.now().plusDays(1))
                .categoryId(1)
                .priorityId(1)
                .statusId(1)
                .userId(1)
                .build();

        var updatedTask = Task
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

        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));
        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(taskCategoryRepository.findById(1)).thenReturn(Optional.of(existingCategory));
        when(taskPriorityRepository.findById(1)).thenReturn(Optional.of(existingPriority));
        when(taskStatusRepository.findById(1)).thenReturn(Optional.of(existingStatus));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        var response = taskServiceImpl.update(existingTask.getId(), request);

        assertNotNull(response);
        verify(taskRepository).save(any(Task.class));

        assertEquals(TaskMapper.mapToDto(updatedTask), response);
    }

    @Test
    void shouldUpdateTask()
    {
        var newUser = User
                .builder()
                .id(2)
                .email("aleksandar.developer@protonmail.com")
                .build();

        var request = TaskRequestDto
                .builder()
                .title("New Title")
                .description("New Description")
                .dueDate(LocalDateTime.now().plusDays(1))
                .categoryId(1)
                .priorityId(1)
                .statusId(1)
                .userId(2)
                .build();

        var updatedTask = Task
                .builder()
                .id(1)
                .title("New Title")
                .description("New Description")
                .dueDate(LocalDateTime.now().plusDays(1))
                .category(existingCategory)
                .priority(existingPriority)
                .status(existingStatus)
                .user(newUser)
                .build();

        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));
        when(userRepository.findById(2)).thenReturn(Optional.of(newUser));
        when(taskCategoryRepository.findById(1)).thenReturn(Optional.of(existingCategory));
        when(taskPriorityRepository.findById(1)).thenReturn(Optional.of(existingPriority));
        when(taskStatusRepository.findById(1)).thenReturn(Optional.of(existingStatus));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        var response = taskServiceImpl.update(existingTask.getId(), request);

        assertNotNull(response);
        verify(taskRepository).save(any(Task.class));
        verify(emailService).sendReassignedTaskEmail(newUser.getEmail(), updatedTask);

        assertEquals(TaskMapper.mapToDto(updatedTask), response);
    }

    @Test
    void taskDeleteShouldFailTaskNotFound()
    {
        when(taskRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> taskServiceImpl.delete(1));
    }

    @Test
    void shouldDeleteTask()
    {
        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));

        taskServiceImpl.delete(1);

        assertNotNull(existingTask);
        verify(taskRepository).delete(any(Task.class));
    }

    @Test
    void taskCompleteShouldFailTaskNotFound()
    {
        when(taskRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> taskServiceImpl.completeTask(1));
    }

    @Test
    void taskDeleteShouldFailStatusNotFound()
    {
        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));
        when(taskStatusRepository.findByName("Completed")).thenReturn(Optional.empty());

        assertNotNull(existingTask);
        assertThrows(NoSuchElementException.class, () -> taskServiceImpl.completeTask(1));
    }

    @Test
    void shouldCompleteTask()
    {
        var completedStatus = TaskStatus
                .builder()
                .id(1)
                .name("Completed")
                .build();

        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));
        when(taskStatusRepository.findByName("Completed")).thenReturn(Optional.of(completedStatus));

        assertNotNull(existingTask);
        assertNotNull(completedStatus);

        var response = taskServiceImpl.completeTask(existingTask.getId());

        assertNotNull(response);
        verify(taskRepository).save(any(Task.class));

        assertEquals(TaskMapper.mapToDto(existingTask), response);
    }
}
