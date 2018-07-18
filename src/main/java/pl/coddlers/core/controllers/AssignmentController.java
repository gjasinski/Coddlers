package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coddlers.git.services.GitProjectService;
import pl.coddlers.core.models.dto.AssignmentDto;
import pl.coddlers.core.services.AssignmentService;

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

	@Autowired
	private GitProjectService gitProjectService;

	@PostMapping
	public ResponseEntity<Long> createAssignment(@Valid @RequestBody AssignmentDto assignmentDto) {
		// TODO this code is only for prototype purposes
		long tutorGitId = 20;
		long gitTutorProjectId = gitProjectService.createCourse(tutorGitId, assignmentDto.getTitle());
		long studentId = 19;
		long gitStudentProjectId = gitProjectService.addStudentToCourse(gitTutorProjectId, studentId);

		// TODO only for prototype purposes
		assignmentDto.setGitStudentProjectId(gitStudentProjectId);
		Long id = assignmentService.createAssignment(assignmentDto);

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

	@RequestMapping(method = RequestMethod.PUT, value = "{id}")
	public ResponseEntity<AssignmentDto> updateAssignment(@PathVariable Long id, @Valid @RequestBody AssignmentDto assignmentDto) {
		return ResponseEntity.ok(assignmentService.updateAssigment(id, assignmentDto));
	}
}
