package com.aleksandar.exafy.service;

import com.aleksandar.exafy.data.dtos.TaskResponseDto;
import com.aleksandar.exafy.data.entities.Task;

public interface EmailService {
    void sendNewTaskEmail(String recipient, Task task);
    void sendReassignedTaskEmail(String recipient, Task task);
    void notifyAboutExistingTask(String recipient, Task task);
}
