package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coddlers.core.models.dto.SubmissionDto;
import pl.coddlers.core.models.entity.Submission;
import pl.coddlers.core.services.SubmissionService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    @Autowired
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    @GetMapping(params = {"taskId"})
    public ResponseEntity<Collection<SubmissionDto>> getSubmissions(@RequestParam(value = "taskId") Long taskId) {
        return ResponseEntity.ok(submissionService.getAllTaskSubmissions(taskId));
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping(params = {"lessonId", "courseEditionId"})
    public ResponseEntity<Collection<SubmissionDto>> getStudentSubmission(@RequestParam(value = "courseEditionId") Long courseEditionId,
                                                              @RequestParam(value = "lessonId") Long lessonId) {
        return ResponseEntity.ok(submissionService.getTaskSubmission(lessonId, courseEditionId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createSubmission(@Valid @RequestBody SubmissionDto submissionDto) {
        Submission submission = submissionService.createSubmission(submissionDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(submission.getId()).toUri();


        return ResponseEntity.created(location).build();
    }
}
