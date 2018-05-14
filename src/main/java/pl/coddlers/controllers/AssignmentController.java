package pl.coddlers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.coddlers.models.dto.AssignmentDto;
import pl.coddlers.services.AssignmentService;

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
	                                             @RequestBody AssignmentDto assignment) {

		return new ResponseEntity<>(assignmentService.createAssignment(courseId, assignment), HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<AssignmentDto>> getAssignments(@PathVariable Long courseId) {
		return new ResponseEntity<>(assignmentService.getAllCoursesAssignments(courseId), HttpStatus.OK);
	}
}
