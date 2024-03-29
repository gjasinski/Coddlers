package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coddlers.core.models.dto.CourseDto;
import pl.coddlers.core.models.entity.Course;
import pl.coddlers.core.services.CourseService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

	private final CourseService courseService;

	@Autowired
	public CourseController(CourseService courseService) {
		this.courseService = courseService;
	}

	@PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<Void> createCourse(@Valid @RequestBody CourseDto courseDto) {
		Course course = courseService.createCourse(courseDto);

		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(course.getId()).toUri();


		return ResponseEntity.created(location).build();
	}

	@GetMapping
	public ResponseEntity<Collection<CourseDto>> getCourses() {
		return ResponseEntity.ok(courseService.getCourses());
	}

	// TODO should be secured, it cannot be secured with simple annotation
	@GetMapping(value = "/{id}")
	public ResponseEntity<CourseDto> getCourse(@PathVariable Long id) {
		return ResponseEntity.ok(courseService.getCourseById(id));
	}

	@PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
	@PutMapping
	public ResponseEntity<Void> updateCourse(@Valid @RequestBody CourseDto courseDto) {
		courseService.updateCourse(courseDto);

		return ResponseEntity.ok().build();
	}

	@GetMapping(params = {"editionId"})
	public ResponseEntity<CourseDto> getCourseByCourseEdition(@RequestParam("editionId") Long id) {
		return ResponseEntity.ok(courseService.getCourseByCourseEditionId(id));

	}
}
