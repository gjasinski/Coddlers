package pl.coddlers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coddlers.models.dto.TaskDto;
import pl.coddlers.services.TaskService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Void> createTask(@Valid @RequestBody TaskDto taskDto) {
        Long id = taskService.createTask(taskDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(params = {"assignmentId"})
    public ResponseEntity<Collection<TaskDto>> getTasks(@RequestParam(value = "assignmentId") Long assignmentId) {
        return ResponseEntity.ok(taskService.getAllAssignmentsTasks(assignmentId));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PutMapping
    public ResponseEntity<Void> updateTask(@Valid @RequestBody TaskDto taskDto) {
        taskService.updateTask(taskDto);

        return ResponseEntity.ok().build();
    }
}
