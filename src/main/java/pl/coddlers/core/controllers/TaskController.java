package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coddlers.git.services.GitTaskService;
import pl.coddlers.core.models.dto.TaskDto;
import pl.coddlers.core.models.entity.Task;
import pl.coddlers.core.services.TaskService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private GitTaskService gitTaskService;

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<Void> createTask(@Valid @RequestBody TaskDto taskDto) {
        Task task = taskService.createTask(taskDto);
        // TODO only for prototype purposes
        long gitStudentProjectId = task.getAssignment().getGitStudentProjectId();
        gitTaskService.createTask(gitStudentProjectId, task.getTitle().replaceAll("\\s+","-"));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(task.getId()).toUri();

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

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDto taskDto) {
        taskService.updateTask(id, taskDto);

        return ResponseEntity.ok().build();
    }
}
