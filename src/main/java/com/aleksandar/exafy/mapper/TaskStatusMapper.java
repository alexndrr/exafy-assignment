package com.aleksandar.exafy.mapper;

import com.aleksandar.exafy.data.dtos.TaskStatusResponseDto;
import com.aleksandar.exafy.data.entities.TaskStatus;

public class TaskStatusMapper {

    public static TaskStatusResponseDto mapToDto(TaskStatus item)
    {
        if (item == null) {
            return null;
        }

        return TaskStatusResponseDto
                .builder()
                .id(item.getId())
                .name(item.getName())
                .triggersNotifications(item.getTriggersNotifications())
                .build();
    }

    public static TaskStatus mapResponseToEntity(TaskStatusResponseDto dto)
    {
        return TaskStatus
                .builder()
                .id(dto.getId())
                .name(dto.getName())
                .triggersNotifications(dto.getTriggersNotifications())
                .build();
    }
}
