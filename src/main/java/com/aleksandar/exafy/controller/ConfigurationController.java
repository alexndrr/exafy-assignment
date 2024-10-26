package com.aleksandar.exafy.controller;

import com.aleksandar.exafy.data.dtos.TaskStatusResponseDto;
import com.aleksandar.exafy.service.ConfigurationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/config")
@SecurityRequirement(name = "basicAuth")
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    @PostMapping("/status-notifications")
    public ResponseEntity<List<TaskStatusResponseDto>> configureNotificationTriggers(@RequestBody List<TaskStatusResponseDto> dto)
    {
        return ResponseEntity.ok(configurationService.configureNotificationTriggers(dto));
    }
}
