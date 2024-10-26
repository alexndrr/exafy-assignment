package com.aleksandar.exafy.mapper;

import com.aleksandar.exafy.data.dtos.TaskCategoryResponseDto;
import com.aleksandar.exafy.data.entities.TaskCategory;

public class TaskCategoryMapper {

    public static TaskCategoryResponseDto mapToDto(TaskCategory item)
    {
        if (item == null) {
            return null;
        }

        return TaskCategoryResponseDto
                .builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }

    public static TaskCategory mapResponseToEntity(TaskCategoryResponseDto dto)
    {
        return TaskCategory
                .builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
