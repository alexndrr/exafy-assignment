package com.aleksandar.exafy.data.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskCategoryResponseDto {
    private Integer id;
    private String name;
}
