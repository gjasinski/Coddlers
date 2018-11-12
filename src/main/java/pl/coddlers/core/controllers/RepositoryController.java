package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coddlers.core.services.StudentLessonRepositoryService;

@RestController
@RequestMapping("/api/repositories")
public class RepositoryController {

    private final StudentLessonRepositoryService studentLessonRepositoryService;

    @Autowired
    public RepositoryController(StudentLessonRepositoryService studentLessonRepositoryService) {
        this.studentLessonRepositoryService = studentLessonRepositoryService;
    }

    @GetMapping(path = "/students", params = {"courseEditionId", "lessonId"})
    public ResponseEntity<String> updateLesson(@RequestParam("courseEditionId") Long courseEditionId, @RequestParam("lessonId") Long lessonId) {
        return ResponseEntity.ok(studentLessonRepositoryService.getRepositoryUrl(courseEditionId, lessonId));
    }
}
