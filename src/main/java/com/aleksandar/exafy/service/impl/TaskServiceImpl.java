package com.aleksandar.exafy.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.aleksandar.exafy.data.dtos.PaginationParametersDto;
import com.aleksandar.exafy.data.dtos.TaskRequestDto;
import com.aleksandar.exafy.data.dtos.TaskResponseDto;
import com.aleksandar.exafy.data.entities.Task;
import com.aleksandar.exafy.mapper.TaskMapper;
import com.aleksandar.exafy.repository.*;
import com.aleksandar.exafy.service.EmailService;
import com.aleksandar.exafy.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private final TaskRepository taskRepository;

    @Autowired
    private final TaskCategoryRepository taskCategoryRepository;

    @Autowired
    private final TaskPriorityRepository taskPriorityRepository;

    @Autowired
    private final TaskStatusRepository taskStatusRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final EmailService emailService;

    public TaskServiceImpl(
            TaskRepository taskRepository,
            TaskCategoryRepository taskCategoryRepository,
            TaskPriorityRepository taskPriorityRepository,
            TaskStatusRepository taskStatusRepository,
            UserRepository userRepository,
            EmailService emailService
    )
    {
        this.taskRepository = taskRepository;
        this.taskCategoryRepository = taskCategoryRepository;
        this.taskPriorityRepository = taskPriorityRepository;
        this.taskStatusRepository = taskStatusRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public Page<TaskResponseDto> getAllPaginated(Integer statusId, PaginationParametersDto pagination) {

        List<String> allowedSortFields = Arrays.asList("dueDate", "priority.name");

        if (!allowedSortFields.contains(pagination.getSortBy())) {
            throw new IllegalArgumentException("Invalid sort field: " + pagination.getSortBy());
        }

        Sort sort = Sort.by(Sort.Direction.fromString(pagination.getSortDirection()), pagination.getSortBy());
        PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getSize(), sort);

        Page<Task> pagedTasksList = taskRepository.findAllByStatusId(statusId, pageRequest);

        if (pagedTasksList.isEmpty())
            return new PageImpl<>(List.of());

        return pagedTasksList.map(TaskMapper::mapToDto);
    }

    @Override
    public List<TaskResponseDto> getAllActiveTasks() {
        return taskRepository
                .findAllActiveTasks(LocalDateTime.now())
                .stream()
                .map(TaskMapper::mapToDto)
                .toList();
    }

    @Override
    public TaskResponseDto create(TaskRequestDto dto) {
        if (StringUtil.isNullOrEmpty(dto.getTitle()))
            throw new IllegalArgumentException("Title field is required.");

        if (dto.getDueDate().isEqual(LocalDateTime.now()) || dto.getDueDate().isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Task due date is invalid.");

        var user = userRepository
                .findById(dto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User with provided id was not found."));

        var category = taskCategoryRepository
                .findById(dto.getCategoryId())
                .orElseThrow(() -> new NoSuchElementException("Category with provided id was not found."));

        var priority = taskPriorityRepository
                .findById(dto.getPriorityId())
                .orElseThrow(() -> new NoSuchElementException("Priority with provided id was not found."));

        var status = taskStatusRepository
                .findById(dto.getStatusId())
                .orElseThrow(() -> new NoSuchElementException("Status with provided id was not found."));


        var newTask = taskRepository.save(TaskMapper.mapRequestToEntity(dto, category, priority, status, user));

        emailService.sendNewTaskEmail(user.getEmail(), newTask);

        return TaskMapper.mapToDto(newTask);
    }

    @Override
    public TaskResponseDto update(Integer id, TaskRequestDto dto) {
        var task = taskRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task with provided id was not found."));

        if (StringUtil.isNullOrEmpty(dto.getTitle()))
            throw new IllegalArgumentException("Title field is required.");

        if (dto.getDueDate().isEqual(LocalDateTime.now()) || dto.getDueDate().isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Task due date is invalid.");

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());

        if (!Objects.equals(task.getCategory().getId(), dto.getCategoryId()))
        {
            var category = taskCategoryRepository
                    .findById(dto.getCategoryId())
                    .orElseThrow(() -> new NoSuchElementException("Category with provided id was not found."));

            task.setCategory(category);
        }

        if (!Objects.equals(task.getPriority().getId(), dto.getPriorityId()))
        {
            var priority = taskPriorityRepository
                    .findById(dto.getPriorityId())
                    .orElseThrow(() -> new NoSuchElementException("Priority with provided id was not found."));

            task.setPriority(priority);
        }

        if (!Objects.equals(task.getStatus().getId(), dto.getStatusId()))
        {
            var status = taskStatusRepository
                    .findById(dto.getStatusId())
                    .orElseThrow(() -> new NoSuchElementException("Status with provided id was not found."));

            task.setStatus(status);
        }

        if (!Objects.equals(task.getUser().getId(), dto.getUserId()))
        {
            var user = userRepository
                    .findById(dto.getUserId())
                    .orElseThrow(() -> new NoSuchElementException("User with provided id was not found."));

            task.setUser(user);

            //Send email notifying user he got assigned new task
            emailService.sendReassignedTaskEmail(user.getEmail(), task);
        }

        taskRepository.save(task);

        return TaskMapper.mapToDto(task);
    }

    @Override
    public void delete(Integer id) {
        var task = taskRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task with provided id was not found."));

        taskRepository.delete(task);
    }

    @Override
    public TaskResponseDto completeTask(Integer id) {
        var task = taskRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task with provided id was not found."));

        var completedStatus = taskStatusRepository
                .findByName("Completed")
                .orElseThrow(() -> new NoSuchElementException("Status with provided id was not found."));

        task.setStatus(completedStatus);

        taskRepository.save(task);

        return TaskMapper.mapToDto(task);
    }
}
