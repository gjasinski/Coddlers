package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coddlers.core.models.dto.CourseVersionDto;
import pl.coddlers.core.services.CourseVersionService;

import java.util.Collection;

@RestController
@RequestMapping("/api/course-versions")
public class CourseVersionController {

    private CourseVersionService courseVersionService;

    @Autowired
    public CourseVersionController(CourseVersionService courseVersionService) {
        this.courseVersionService = courseVersionService;
    }

    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @GetMapping(params = {"courseId"})
    public ResponseEntity<Collection<CourseVersionDto>> getCourseVersions(@RequestParam Long courseId) {
        return ResponseEntity.ok(courseVersionService.getCourseVersionsByCourseId(courseId));
    }
}
