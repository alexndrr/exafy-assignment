package com.aleksandar.exafy.data.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
    private Integer id;
    private String email;
}
