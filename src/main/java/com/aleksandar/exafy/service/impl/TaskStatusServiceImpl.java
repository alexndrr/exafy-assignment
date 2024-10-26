package com.aleksandar.exafy.service.impl;

import com.aleksandar.exafy.data.dtos.TaskStatusResponseDto;
import com.aleksandar.exafy.mapper.TaskStatusMapper;
import com.aleksandar.exafy.repository.TaskStatusRepository;
import com.aleksandar.exafy.service.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskStatusServiceImpl implements TaskStatusService {

    @Autowired
    private final TaskStatusRepository taskStatusRepository;

    public TaskStatusServiceImpl(TaskStatusRepository taskStatusRepository)
    {
        this.taskStatusRepository = taskStatusRepository;
    }

    @Override
    public List<TaskStatusResponseDto> getAll() {
        return taskStatusRepository
                .findAll()
                .stream()
                .map(TaskStatusMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
