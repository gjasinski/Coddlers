package pl.coddlers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coddlers.git.services.GitProjectService;
import pl.coddlers.models.dto.CourseDto;
import pl.coddlers.models.entity.Course;
import pl.coddlers.services.CourseService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<Void> createCourse(@Valid @RequestBody CourseDto courseDto) {
        Course course = courseService.createCourse(courseDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(course.getId()).toUri();


        return ResponseEntity.created(location).build();
    }

    @GetMapping(params = {"startsAt", "number"})
    public ResponseEntity<Collection<CourseDto>> getCourses(@RequestParam(value = "startsAt", required = false) Integer startsAt,
                                                            @RequestParam(value = "number", required = false) Integer number) {
        return ResponseEntity.ok(courseService.getCourses(startsAt, number));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PutMapping
    public ResponseEntity<Void> updateCourse(@Valid @RequestBody CourseDto courseDto) {
        courseService.updateCourse(courseDto);

        return ResponseEntity.ok().build();
    }
}
