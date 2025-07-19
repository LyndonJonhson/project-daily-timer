package com.projeto.dailytime.controllers;

import com.projeto.dailytime.domain.task.Task;
import com.projeto.dailytime.domain.task.TaskDTO;
import com.projeto.dailytime.infra.security.TokenService;
import com.projeto.dailytime.repositories.TaskRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskRepository taskRepository;
    private final TokenService tokenService;

    public TaskController(
            TaskRepository taskRepository,
            TokenService tokenService
    ) {
        this.taskRepository = taskRepository;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid TaskDTO dto, HttpServletRequest request) {
        var user = tokenService.getUser(request);

        var newTask = Task.builder()
                .title(dto.title())
                .description(dto.description())
                .user(user)
                .build();

        taskRepository.save(newTask);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Page<Task>> read(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request
    ) {
        var user = tokenService.getUser(request);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(taskRepository.findAllByUserId(user.getId(), pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody @Valid TaskDTO dto,
            HttpServletRequest request
    ) {
        var opt = taskRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.badRequest().build();

        var task = opt.get();
        var user = tokenService.getUser(request);

        if (!task.getUser().getId().equals(user.getId())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        task.setTitle(dto.title());
        task.setDescription(dto.description());

        taskRepository.save(task);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        var opt = taskRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.badRequest().build();

        var task = opt.get();
        var user = tokenService.getUser(request);

        if (!task.getUser().getId().equals(user.getId())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        taskRepository.delete(task);
        return ResponseEntity.noContent().build();
    }
}
