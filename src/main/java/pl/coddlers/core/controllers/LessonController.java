package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coddlers.core.models.dto.LessonDto;
import pl.coddlers.core.services.LessonService;
import pl.coddlers.git.services.GitLessonService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonService lessonService;

    private final GitLessonService gitProjectService;

    @Autowired
    public LessonController(LessonService lessonService, GitLessonService gitProjectService) {
        this.lessonService = lessonService;
        this.gitProjectService = gitProjectService;
    }

    @PostMapping
    public ResponseEntity<Long> createLesson(@Valid @RequestBody LessonDto lessonDto) throws ExecutionException, InterruptedException {
        // TODO this code is only for prototype purposes
        long tutorGitId = 20;
        long studentId = 19;
        Long gitStudentProjectId = gitProjectService.createLesson(tutorGitId, lessonDto.getTitle())
                .thenCompose((gitTutorProjectId) -> gitProjectService.forkLesson(gitTutorProjectId, studentId)).get();
        // TODO only for prototype purposes
        lessonDto.setGitStudentProjectId(gitStudentProjectId);

        Long id = lessonService.createLesson(lessonDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(params = {"courseId"})
    public ResponseEntity<Collection<LessonDto>> getLessons(@RequestParam(value = "courseId") Long courseId) {
        return ResponseEntity.ok(lessonService.getAllCoursesLessons(courseId));
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<LessonDto> getLesson(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getLessonById(id));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public ResponseEntity<LessonDto> updateLesson(@PathVariable Long id, @Valid @RequestBody LessonDto lessonDto) {
        return ResponseEntity.ok(lessonService.updateLesson(id, lessonDto));
    }
}
