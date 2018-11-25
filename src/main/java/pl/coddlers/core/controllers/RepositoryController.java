package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coddlers.core.services.LessonService;
import pl.coddlers.core.services.StudentLessonRepositoryService;

@RestController
@RequestMapping("/api/repositories")
public class RepositoryController {

    private final StudentLessonRepositoryService studentLessonRepositoryService;
    private final LessonService lessonService;

    @Autowired
    public RepositoryController(StudentLessonRepositoryService studentLessonRepositoryService, LessonService lessonService) {
        this.studentLessonRepositoryService = studentLessonRepositoryService;
        this.lessonService = lessonService;
    }

    @GetMapping(path = "/students", params = {"courseEditionId", "lessonId"})
    public ResponseEntity<String> getStudentLessonRepository(@RequestParam("courseEditionId") Long courseEditionId, @RequestParam("lessonId") Long lessonId) {
        return ResponseEntity.ok(studentLessonRepositoryService.getRepositoryUrl(courseEditionId, lessonId));
    }

    @GetMapping(path = "/teachers", params = {"lessonId"})
    public ResponseEntity<String> getTeacherLessonRepository(@RequestParam("lessonId") Long lessonId) {
        return ResponseEntity.ok(lessonService.getLessonRepositoryUrl(lessonId));
    }
}
