package com.aleksandar.exafy.mapper;

import com.aleksandar.exafy.data.dtos.TaskPriorityResponseDto;
import com.aleksandar.exafy.data.entities.TaskPriority;

public class TaskPriorityMapper {

    public static TaskPriorityResponseDto mapToDto(TaskPriority item)
    {
        if (item == null) {
            return null;
        }

        return TaskPriorityResponseDto
                .builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }

    public static TaskPriority mapResponseToEntity(TaskPriorityResponseDto dto)
    {
        return TaskPriority
                .builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
