package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.coddlers.core.models.dto.CourseEditionDto;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.services.CourseEditionService;

import java.util.List;

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

	@PreAuthorize("hasRole('ROLE_STUDENT')")
	@GetMapping
	public ResponseEntity<List<CourseEditionDto>> getCourses(){
		return ResponseEntity.ok(courseEditionService.getCourses());
	}

}
