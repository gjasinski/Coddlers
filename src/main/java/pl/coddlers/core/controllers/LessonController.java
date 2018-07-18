package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coddlers.git.services.GitProjectService;
import pl.coddlers.core.models.dto.LessonDTO;
import pl.coddlers.core.services.LessonService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

	private final LessonService lessonService;

	private final GitProjectService gitProjectService;

	@Autowired
	public LessonController(LessonService lessonService, GitProjectService gitProjectService) {
		this.lessonService = lessonService;
		this.gitProjectService = gitProjectService;
	}

	@PostMapping
	public ResponseEntity<Long> createLesson(@Valid @RequestBody LessonDTO lessonDTO) {
		// TODO this code is only for prototype purposes
		long tutorGitId = 20;
		long gitTutorProjectId = gitProjectService.createCourse(tutorGitId, lessonDTO.getTitle());
		long studentId = 19;
		long gitStudentProjectId = gitProjectService.addStudentToCourse(gitTutorProjectId, studentId);

		// TODO only for prototype purposes
		lessonDTO.setGitStudentProjectId(gitStudentProjectId);
		Long id = lessonService.createLesson(lessonDTO);

		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(id)
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@GetMapping(params = {"courseId"})
	public ResponseEntity<Collection<LessonDTO>> getLessons(@RequestParam(value = "courseId") Long courseId) {
		return ResponseEntity.ok(lessonService.getAllCoursesLessons(courseId));
	}

	@GetMapping(value = "{id}")
	public ResponseEntity<LessonDTO> getLesson(@PathVariable Long id) {
		return ResponseEntity.ok(lessonService.getLessonById(id));
	}

	@RequestMapping(method = RequestMethod.PUT, value = "{id}")
	public ResponseEntity<LessonDTO> updateLesson(@PathVariable Long id, @Valid @RequestBody LessonDTO lessonDTO) {
		return ResponseEntity.ok(lessonService.updateLesson(id, lessonDTO));
	}
}
