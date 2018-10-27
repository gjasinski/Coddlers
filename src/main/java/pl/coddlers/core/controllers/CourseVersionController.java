package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coddlers.core.models.dto.CourseDto;
import pl.coddlers.core.models.dto.CourseEditionDto;
import pl.coddlers.core.models.dto.CourseVersionDto;
import pl.coddlers.core.services.CourseEditionService;
import pl.coddlers.core.services.CourseVersionService;

import java.util.Collection;

@RestController
@RequestMapping("/api/course-versions")
public class CourseVersionController {

    private CourseVersionService courseVersionService;
    private CourseEditionService courseEditionService;

    @Autowired
    public CourseVersionController(CourseVersionService courseVersionService, CourseEditionService courseEditionService) {
        this.courseVersionService = courseVersionService;
        this.courseEditionService = courseEditionService;
    }

    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @GetMapping(params = {"courseId"})
    public ResponseEntity<Collection<CourseVersionDto>> getCourseVersions(@RequestParam Long courseId) {
        return ResponseEntity.ok(courseVersionService.getCourseVersionsByCourseId(courseId));
    }

    @GetMapping(value = "/{courseVersionId}/editions")
    public ResponseEntity<Collection<CourseEditionDto>> getCourseEditions(@PathVariable Long courseVersionId){
        return ResponseEntity.ok(courseEditionService.getCourseEditionsByCourseVersionId(courseVersionId));
    }

    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<CourseVersionDto> createCourseVersion(@RequestBody CourseDto course){
        return ResponseEntity.ok(courseVersionService.createCourseVersion(course.getId()));
    }
}
