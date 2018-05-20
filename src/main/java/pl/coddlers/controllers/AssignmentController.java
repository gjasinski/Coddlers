package pl.coddlers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coddlers.models.dto.AssignmentDto;
import pl.coddlers.services.AssignmentService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/courses/{courseId}/assignments")
public class AssignmentController {

	private final AssignmentService assignmentService;

	@Autowired
	public AssignmentController(AssignmentService assignmentService) {
		this.assignmentService = assignmentService;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Long> createAssignment(@PathVariable Long courseId,
	                                             @Valid @RequestBody AssignmentDto assignment) {
		Long id = assignmentService.createAssignment(courseId, assignment);

		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(id).toUri();

		return ResponseEntity.created(location).build();
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Collection<AssignmentDto>> getAssignments(@PathVariable Long courseId) {
		return ResponseEntity.ok(assignmentService.getAllCoursesAssignments(courseId));
	}
}
