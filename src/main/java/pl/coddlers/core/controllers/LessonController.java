package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coddlers.core.exceptions.WrongParametersException;
import pl.coddlers.core.models.dto.LessonDto;
import pl.coddlers.core.models.entity.StudentLessonRepository;
import pl.coddlers.core.services.LessonService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonService lessonService;


    @Autowired
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Long> createLesson(@Valid @RequestBody LessonDto lessonDto) {
        Long id = lessonService.createLesson(lessonDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // TODO prevent students and teachers to get lessons not assigned to them
    @GetMapping(params = {"courseId", "courseVersion", "courseEditionId"})
    public ResponseEntity<Collection<LessonDto>> getLessons(@RequestParam(value = "courseId", required = false) Long courseId,
                                                            @RequestParam(value = "courseVersion", required = false) Integer courseVersion,
                                                            @RequestParam(value = "courseEditionId", required = false) Long courseEditionId) {
        if (courseId == null && courseVersion == null && courseEditionId == null) {
            throw new WrongParametersException("You should provide either courseId and optionally courseVersion or " +
                    "courseEditionId to get lessons.");
        }
        if (courseEditionId != null) {
            return ResponseEntity.ok(lessonService.getLessonsByCourseEditionId(courseEditionId));
        }
        return ResponseEntity.ok(lessonService.getAllCourseVersionLessons(courseId, courseVersion));
    }

    // TODO prevent students and teachers to get lessons not assigned to them
    @GetMapping(value = "{id}")
    public ResponseEntity<LessonDto> getLesson(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getLessonById(id));
    }

    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public ResponseEntity<LessonDto> updateLesson(@PathVariable Long id, @Valid @RequestBody LessonDto lessonDto) {
        return ResponseEntity.ok(lessonService.updateLesson(id, lessonDto));
    }

    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @GetMapping(params = {"courseEditionId", "lessonId"})
    public ResponseEntity<String> forkLessons(@RequestParam(value = "courseEditionId", required = true) Long courseEditionId,
                                                                     @RequestParam(value = "lessonId", required = true) Long lessonId) {

        lessonService.forkLessons(courseEditionId, lessonId);
        return ResponseEntity.ok("ok");
    }
}
