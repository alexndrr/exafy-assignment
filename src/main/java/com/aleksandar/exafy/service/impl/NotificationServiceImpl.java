package com.aleksandar.exafy.service.impl;

import com.aleksandar.exafy.data.entities.Task;
import com.aleksandar.exafy.mapper.TaskMapper;
import com.aleksandar.exafy.service.EmailService;
import com.aleksandar.exafy.service.NotificationService;
import com.aleksandar.exafy.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private final TaskService taskService;

    @Autowired
    private final EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    public NotificationServiceImpl(
            TaskService taskService,
            EmailService emailService)
    {
        this.taskService = taskService;
        this.emailService = emailService;
    }

    private final Map<Integer, LocalDateTime> lastNotificationTimeMap = new HashMap<>();
    private static final Map<String, Long> notificationFrequencies = Map.of(
            "Low", 60L,
            "Medium", 30L,
            "High", 10L
    );

    @Scheduled(cron = "*/30 * * * * *")
    @Override
    public void sendNotifications() {

        logger.info("STARTED sendNotifications cron job");
        var tasksList = taskService.getAllActiveTasks();

        for (var item : tasksList)
        {
            if (!item.getStatusDto().getTriggersNotifications())
                continue;

            LocalDateTime lastNotificationTime = lastNotificationTimeMap.get(item.getId());
            LocalDateTime currentTime = LocalDateTime.now();

            Long frequency = notificationFrequencies.get(item.getPriorityDto().getName());

            if (lastNotificationTime == null || lastNotificationTime.plusSeconds(frequency).isBefore(currentTime)) {

                logger.info("Sending email to {} with task priority of {}.",
                        item.getUserDto().getEmail(),
                        item.getPriorityDto().getName());

                Task task = TaskMapper.mapResponseToEntity(item);
                emailService.notifyAboutExistingTask(task.getUser().getEmail(), task);

                lastNotificationTimeMap.put(item.getId(), currentTime);
            }
        }
    }
}
