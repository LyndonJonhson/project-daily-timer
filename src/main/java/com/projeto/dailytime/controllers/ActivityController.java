package com.projeto.dailytime.controllers;

import com.projeto.dailytime.domain.activity.Activity;
import com.projeto.dailytime.domain.activity.ActivityDTO;
import com.projeto.dailytime.domain.task.Task;
import com.projeto.dailytime.domain.task.TaskIdDTO;
import com.projeto.dailytime.infra.security.TokenService;
import com.projeto.dailytime.repositories.ActivityRepository;
import com.projeto.dailytime.repositories.TaskRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    private final ActivityRepository activityRepository;
    private final TaskRepository taskRepository;
    private final TokenService tokenService;

    public ActivityController(
            ActivityRepository activityRepository,
            TaskRepository taskRepository,
            TokenService tokenService
    ) {
        this.activityRepository = activityRepository;
        this.taskRepository = taskRepository;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid ActivityDTO dto, HttpServletRequest request) {
        var taskOpt = taskRepository.findById(dto.taskId());
        if (taskOpt.isEmpty()) return ResponseEntity.badRequest().build();

        var task = taskOpt.get();
        var user = tokenService.getUser(request);

        if (!task.getUser().getId().equals(user.getId())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        var newActivity = Activity.builder()
                .text(dto.text())
                .task(task)
                .build();

        activityRepository.save(newActivity);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Activity>> read(@RequestBody @Valid TaskIdDTO dto, HttpServletRequest request) {
        var taskOpt = taskRepository.findById(dto.id());
        if (taskOpt.isEmpty()) return ResponseEntity.badRequest().build();

        var task = taskOpt.get();
        var user = tokenService.getUser(request);

        if (!task.getUser().getId().equals(user.getId())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(activityRepository.findAllByTaskId(dto.id()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody @Valid ActivityDTO dto,
            HttpServletRequest request
    ) {
        var opt = activityRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.badRequest().build();

        var taskOpt = taskRepository.findById(dto.taskId());
        if (taskOpt.isEmpty()) return ResponseEntity.badRequest().build();

        var activity = opt.get();
        var user = tokenService.getUser(request);

        if (!activity.getTask().getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        activity.setText(dto.text());
        activity.setTask(Task.builder().id(dto.taskId()).build());

        activityRepository.save(activity);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        var opt = activityRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.badRequest().build();

        var activity = opt.get();
        var user = tokenService.getUser(request);

        if (!activity.getTask().getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        activityRepository.delete(activity);
        return ResponseEntity.noContent().build();
    }
}
