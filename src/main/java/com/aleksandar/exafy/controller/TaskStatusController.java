package com.aleksandar.exafy.controller;

import com.aleksandar.exafy.data.dtos.TaskStatusResponseDto;
import com.aleksandar.exafy.service.TaskStatusService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/task-statuses")
@SecurityRequirement(name = "basicAuth")
public class TaskStatusController {

    @Autowired
    private TaskStatusService taskStatusService;

    @GetMapping
    public ResponseEntity<List<TaskStatusResponseDto>> getAll()
    {
        return ResponseEntity.ok(taskStatusService.getAll());
    }
}
