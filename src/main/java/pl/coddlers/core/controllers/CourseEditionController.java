package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coddlers.core.models.dto.CourseEditionDto;
import pl.coddlers.core.models.dto.CourseEditionLessonDto;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.services.CourseEditionService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("api/editions")
public class CourseEditionController {

    private final CourseEditionService courseEditionService;

    @Autowired
    public CourseEditionController(CourseEditionService courseEditionService) {
        this.courseEditionService = courseEditionService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CourseEditionDto> getCourseEdition(@PathVariable Long id) {
        return ResponseEntity.ok(courseEditionService.getCourseEditionById(id));
    }

    @GetMapping(value = "/{id}/course-edition-lessons", params = {"lessonId"})
    public ResponseEntity<Collection<CourseEditionLessonDto>> getCourseEditionLessonList(@PathVariable Long id,
                                                                                         @RequestParam(value = "lessonId", required = false) Long lessonId) {
        if (lessonId != null) {
            return ResponseEntity.ok(Collections.singletonList(courseEditionService.getCourseEditionLesson(lessonId, id)));
        }

        return ResponseEntity.ok(courseEditionService.getCourseEditionLessonList(id));
    }

    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/course-edition-lessons/{id}")
    public ResponseEntity<Void> updateCourseEditionLesson(@PathVariable Long id,
                                                          @Valid @RequestBody CourseEditionLessonDto courseEditionLessonDto) {
        courseEditionService.updateCourseEditionLesson(id, courseEditionLessonDto);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createEdition(@RequestBody CourseEditionDto courseEditionDto) throws ExecutionException, InterruptedException {
        CourseEdition courseEdition = courseEditionService.createCourseEdition(courseEditionDto);
        courseEditionService.createCourseEditionLessons(courseEdition);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(courseEdition.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
}
