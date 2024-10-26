package com.aleksandar.exafy.service;

import com.aleksandar.exafy.data.dtos.PaginationParametersDto;
import com.aleksandar.exafy.data.dtos.TaskRequestDto;
import com.aleksandar.exafy.data.dtos.TaskResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService {
    Page<TaskResponseDto> getAllPaginated(Integer statusId, PaginationParametersDto pagination);
    List<TaskResponseDto> getAllActiveTasks();
    TaskResponseDto create(TaskRequestDto dto);
    TaskResponseDto update(Integer id, TaskRequestDto dto);
    void delete(Integer id);
    TaskResponseDto completeTask(Integer id);
}
