package com.aleksandar.exafy.mapper;

import com.aleksandar.exafy.data.dtos.*;
import com.aleksandar.exafy.data.entities.*;

public class TaskMapper {

    public static TaskResponseDto mapToDto(Task task)
    {
        if (task == null) {
            return null;
        }

        return TaskResponseDto
                .builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .categoryDto(TaskCategoryMapper.mapToDto(task.getCategory()))
                .priorityDto(TaskPriorityMapper.mapToDto(task.getPriority()))
                .statusDto(TaskStatusMapper.mapToDto(task.getStatus()))
                .userDto(UserMapper.mapToDto(task.getUser()))
                .build();
    }

    public static Task mapResponseToEntity(TaskResponseDto dto)
    {
        return Task
                .builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .dueDate(dto.getDueDate())
                .category(TaskCategoryMapper.mapResponseToEntity(dto.getCategoryDto()))
                .priority(TaskPriorityMapper.mapResponseToEntity(dto.getPriorityDto()))
                .status(TaskStatusMapper.mapResponseToEntity(dto.getStatusDto()))
                .user(UserMapper.mapResponseToEntity(dto.getUserDto()))
                .build();
    }

    public static Task mapRequestToEntity(
            TaskRequestDto dto,
            TaskCategory category,
            TaskPriority priority,
            TaskStatus status,
            User user)
    {
        return Task
                .builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .dueDate(dto.getDueDate())
                .category(category)
                .priority(priority)
                .status(status)
                .user(user)
                .build();
    }
}
