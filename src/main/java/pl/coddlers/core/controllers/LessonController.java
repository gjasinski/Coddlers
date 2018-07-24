package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coddlers.git.services.GitLessonService;
import pl.coddlers.core.models.dto.LessonDto;
import pl.coddlers.core.services.LessonService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

	private final LessonService lessonService;

	private final GitLessonService gitProjectService;

	@Autowired
	public LessonController(LessonService lessonService, GitLessonService gitProjectService) {
		this.lessonService = lessonService;
		this.gitProjectService = gitProjectService;
	}

	@PostMapping
	public ResponseEntity<Long> createLesson(@Valid @RequestBody LessonDto lessonDto) {
		// TODO this code is only for prototype purposes
		long tutorGitId = 20;
		long gitTutorProjectId = gitProjectService.createLesson(tutorGitId, lessonDto.getTitle());
		long studentId = 19;
		long gitStudentProjectId = gitProjectService.forkLesson(gitTutorProjectId, studentId);

		// TODO only for prototype purposes
		lessonDto.setGitStudentProjectId(gitStudentProjectId);
		Long id = lessonService.createLesson(lessonDto);

		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(id)
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@GetMapping(params = {"courseId"})
	public ResponseEntity<Collection<LessonDto>> getLessons(@RequestParam(value = "courseId") Long courseId) {
		return ResponseEntity.ok(lessonService.getAllCoursesLessons(courseId));
	}

	@GetMapping(value = "{id}")
	public ResponseEntity<LessonDto> getLesson(@PathVariable Long id) {
		return ResponseEntity.ok(lessonService.getLessonById(id));
	}

	@RequestMapping(method = RequestMethod.PUT, value = "{id}")
	public ResponseEntity<LessonDto> updateLesson(@PathVariable Long id, @Valid @RequestBody LessonDto lessonDto) {
		return ResponseEntity.ok(lessonService.updateLesson(id, lessonDto));
	}
}
