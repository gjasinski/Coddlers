package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coddlers.core.exceptions.WrongParametersException;
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

    private final GitTaskService gitTaskService;

    private final TaskService taskService;

    @Autowired
    public TaskController(GitTaskService gitTaskService, TaskService taskService) {
        this.gitTaskService = gitTaskService;
        this.taskService = taskService;
    }

    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createTask(@Valid @RequestBody TaskDto taskDto) {
        Task task = taskService.createTask(taskDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(task.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    // TODO check if user is assigned to lesson
    @GetMapping(params = {"lessonId", "courseEditionLessonId"})
    public ResponseEntity<Collection<TaskDto>> getTasks(@RequestParam(value = "lessonId", required = false) Long lessonId,
                                                        @RequestParam(value = "courseEditionLessonId", required = false) Long courseEditionLessonId) {
        if (courseEditionLessonId != null) {
            return ResponseEntity.ok(taskService.getAllCourseEditionLessonTasks(courseEditionLessonId));
        } else if (lessonId != null) {
            return ResponseEntity.ok(taskService.getAllLessonsTasks(lessonId));
        } else {
            throw new WrongParametersException("You should provide either lessonId or courseEditionLessonId");
        }
    }

    @GetMapping(params = {"courseEditionLessonId"})
    public ResponseEntity<Collection<TaskDto>> getCourseEditionLessonTasks(@RequestParam(value = "courseEditionLessonId") Long courseEditionLessonId) {
        return ResponseEntity.ok(taskService.getAllCourseEditionLessonTasks(courseEditionLessonId));
    }

    // TODO check if user is assigned to lesson connected to this task
    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    // TODO check if user is assigned to lesson connected to this task
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDto taskDto) {
        taskService.updateTask(id, taskDto);

        return ResponseEntity.ok().build();
    }
}
