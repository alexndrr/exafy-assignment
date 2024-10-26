package com.aleksandar.exafy.data.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TaskRequestDto {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Integer categoryId;
    private Integer priorityId;
    private Integer statusId;
    private Integer userId;
}
