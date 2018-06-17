package pl.coddlers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coddlers.models.dto.AssignmentDto;
import pl.coddlers.services.AssignmentService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

	private final AssignmentService assignmentService;

	@Autowired
	public AssignmentController(AssignmentService assignmentService) {
		this.assignmentService = assignmentService;
	}

	@PostMapping
	public ResponseEntity<Long> createAssignment(@Valid @RequestBody AssignmentDto assignment) {
		Long id = assignmentService.createAssignment(assignment);

		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(id).toUri();

		return ResponseEntity.created(location).build();
	}

	@GetMapping(params = {"courseId"})
	public ResponseEntity<Collection<AssignmentDto>> getAssignments(@RequestParam(value = "courseId") Long courseId) {
		return ResponseEntity.ok(assignmentService.getAllCoursesAssignments(courseId));
	}

	@GetMapping(value = "{id}")
	public ResponseEntity<AssignmentDto> getAssignment(@PathVariable Long id) {
		return ResponseEntity.ok(assignmentService.getAssignmentById(id));
	}
}
