package com.aleksandar.exafy.mapper;

import com.aleksandar.exafy.data.dtos.UserResponseDto;
import com.aleksandar.exafy.data.entities.User;

public class UserMapper {


    public static UserResponseDto mapToDto(User item)
    {
        if (item == null) {
            return null;
        }

        return UserResponseDto
                .builder()
                .id(item.getId())
                .email(item.getEmail())
                .build();
    }

    public static User mapResponseToEntity(UserResponseDto dto)
    {
        return User
                .builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .build();
    }
}
