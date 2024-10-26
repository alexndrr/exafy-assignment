package com.aleksandar.exafy.controller;

import com.aleksandar.exafy.data.dtos.PaginationParametersDto;
import com.aleksandar.exafy.data.dtos.TaskRequestDto;
import com.aleksandar.exafy.data.dtos.TaskResponseDto;
import com.aleksandar.exafy.service.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@SecurityRequirement(name = "basicAuth")
public class TaskController {

    @Autowired
    private final TaskService taskService;

    public TaskController(TaskService taskService)
    {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponseDto>> getAll(
            @RequestParam Integer statusId,
            PaginationParametersDto pagination)
    {
        return ResponseEntity.ok(taskService.getAllPaginated(statusId, pagination));
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> create(@RequestBody TaskRequestDto dto)
    {
        return ResponseEntity.ok(taskService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> update(@PathVariable Integer id, @RequestBody TaskRequestDto dto)
    {
        return ResponseEntity.ok(taskService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id)
    {
        taskService.delete(id);
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponseDto> complete(@PathVariable Integer id)
    {
        return ResponseEntity.ok(taskService.completeTask(id));
    }
}
