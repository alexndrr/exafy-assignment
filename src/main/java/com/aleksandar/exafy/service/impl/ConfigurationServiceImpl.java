package com.aleksandar.exafy.service.impl;

import com.aleksandar.exafy.data.dtos.TaskStatusResponseDto;
import com.aleksandar.exafy.mapper.TaskStatusMapper;
import com.aleksandar.exafy.repository.TaskStatusRepository;
import com.aleksandar.exafy.service.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationServiceImpl.class);
    @Autowired
    private final TaskStatusRepository taskStatusRepository;

    public ConfigurationServiceImpl(TaskStatusRepository taskStatusRepository)
    {
        this.taskStatusRepository = taskStatusRepository;
    }

    @Override
    public List<TaskStatusResponseDto> configureNotificationTriggers(List<TaskStatusResponseDto> dto) {

        List<TaskStatusResponseDto> changedStatuses = new ArrayList<>();
        for (var item : dto)
        {
            var existingTaskStatus = taskStatusRepository.findById(item.getId());

            //If provided id doesn't match with any task status just skip it
            if (existingTaskStatus.isEmpty())
            {
                logger.info("Task status with provided id {} was not found.", item.getId());
                continue;
            }

            existingTaskStatus
                    .get()
                    .setTriggersNotifications(item.getTriggersNotifications());

            taskStatusRepository.save(existingTaskStatus.get());
            changedStatuses.add(TaskStatusMapper.mapToDto(existingTaskStatus.get()));
        }

        return changedStatuses;
    }
}
