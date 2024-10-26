package com.aleksandar.exafy.service;

import com.aleksandar.exafy.data.dtos.TaskStatusResponseDto;

import java.util.List;

public interface TaskStatusService {
    List<TaskStatusResponseDto> getAll();
}
