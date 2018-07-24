package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coddlers.core.models.dto.SubmissionDto;
import pl.coddlers.core.services.SubmissionService;

import java.util.Collection;

@RestController
@RequestMapping("api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    @Autowired
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping(params = {"taskId"})
    public ResponseEntity<Collection<SubmissionDto>> getSubmissions(@RequestParam(value = "taskId") Long taskId) {
        return ResponseEntity.ok(submissionService.getAllTasksSubmissions(taskId));
    }
}
