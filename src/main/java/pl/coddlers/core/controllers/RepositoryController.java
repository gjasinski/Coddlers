package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping(path = "/students", params = {"courseEditionId", "lessonId"})
    public ResponseEntity<String> updateLesson(@Param("courseEditionId") Long courseEditionId, @Param("lessonId") Long lessonId) {
        return ResponseEntity.ok(studentLessonRepositoryService.getRepositoryUrl(courseEditionId, lessonId));
    }
}
