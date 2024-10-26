package com.aleksandar.exafy.data.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskResponseDto {
    private Integer id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private TaskCategoryResponseDto categoryDto;
    private TaskPriorityResponseDto priorityDto;
    private TaskStatusResponseDto statusDto;
    private UserResponseDto userDto;
}
