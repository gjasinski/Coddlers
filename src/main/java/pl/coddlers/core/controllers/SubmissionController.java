package pl.coddlers.core.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coddlers.core.exceptions.InternalServerErrorException;
import pl.coddlers.core.exceptions.InvalidPointsAmountException;
import pl.coddlers.core.exceptions.NotSuchCommentTypeSpecifiedException;
import pl.coddlers.core.exceptions.SubmissionStatusChangeException;
import pl.coddlers.core.models.converters.SubmissionConverter;
import pl.coddlers.core.models.dto.*;
import pl.coddlers.core.models.entity.Submission;
import pl.coddlers.core.models.entity.SubmissionStatusType;
import pl.coddlers.core.models.entity.SubmissionStatusTypeEnum;
import pl.coddlers.core.services.CommentService;
import pl.coddlers.core.services.SubmissionService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

import static pl.coddlers.core.models.entity.SubmissionStatusTypeEnum.*;

@Slf4j
@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;
    private final CommentService commentService;
    private final SubmissionConverter submissionConverter;

    @Autowired
    public SubmissionController(SubmissionService submissionService, CommentService commentService, SubmissionConverter submissionConverter) {
        this.submissionService = submissionService;
        this.commentService = commentService;
        this.submissionConverter = submissionConverter;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    @GetMapping(params = {"taskId"})
    public ResponseEntity<Collection<SubmissionDto>> getSubmissions(@RequestParam(value = "taskId") Long taskId) {
        return ResponseEntity.ok(submissionService.getAllTaskSubmissions(taskId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    @GetMapping(params = {"taskId", "courseEditionId"})
    public ResponseEntity<Collection<SubmissionDto>> getSubmissionsForCourseEdition(@RequestParam(value = "taskId") Long taskId,
                                                                                    @RequestParam(value = "courseEditionId") Long courseEdition) {
        return ResponseEntity.ok(submissionService.getAllTaskSubmissionsForCourseEdition(taskId, courseEdition));
    }

    @GetMapping(params = {"lessonId", "courseEditionId"})
    public ResponseEntity<Collection<SubmissionDto>> getStudentSubmission(@RequestParam(value = "courseEditionId") Long courseEditionId,
                                                                          @RequestParam(value = "lessonId") Long lessonId) {
        return ResponseEntity.ok(submissionService.getTaskSubmission(lessonId, courseEditionId));
    }

    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @GetMapping(params = {"submissionId"})
    public ResponseEntity<SubmissionDataDto> getSubmission(@RequestParam(value = "submissionId") Long submissionId) {
        SubmissionDataDto submissionData = new SubmissionDataDto();
        submissionData.setFullName(submissionService.getStudentFullName(submissionId));
        submissionData.setSubmission(submissionConverter.convertFromEntity(submissionService.getSubmissionById(submissionId)));
        submissionData.setGitFileContents(submissionService.getSubmissionContent(submissionId));
        return ResponseEntity.ok(submissionData);
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

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    @PostMapping(path = "/{id}/reopen")
    public ResponseEntity<Void> reopenSubmission(@PathVariable Long id, @Valid @RequestBody SubmissionReopenDto submissionReopenDto) {
        Submission submission = submissionService.getSubmissionById(id);
        if (!(submission.getSubmissionStatusTypeEnum() == GRADED)) {
            throw new SubmissionStatusChangeException(String.format("You can reopen submissions only when they " +
                            "are in state %s. Current submission state is %s", GRADED.getStatus(),
                    submission.getSubmissionStatusTypeEnum().getStatus()));
        }

        submission = saveSubmissionWithNewType(submission, CHANGES_REQUESTED);
        createComment(submissionReopenDto, submission);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    @PostMapping(path = "/{id}/grade")
    public ResponseEntity<Void> gradeSubmission(@PathVariable Long id, @Valid @RequestBody SubmissionGradeDto submissionGradeDto) {
        Submission submission = submissionService.getSubmissionById(id);

        int maxPoints = submission.getTask().getMaxPoints();
        int grade = submissionGradeDto.getPoints();
        if (grade > maxPoints || grade < 0) {
            throw new InvalidPointsAmountException(grade, maxPoints);
        }
        submission.setPoints(submissionGradeDto.getPoints());

        submission = saveSubmissionWithNewType(submission, GRADED);
        createComment(submissionGradeDto, submission);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    @PostMapping(path = "/{id}/request-changes")
    public ResponseEntity<Void> createRequestChangesForSubmission(@PathVariable Long id,
                                                                  @Valid @RequestBody SubmissionRequestChangesDto submissionRequestChangesDto) {
        Submission submission = submissionService.getSubmissionById(id);
        if (!(submission.getSubmissionStatusTypeEnum() == WAITING_FOR_REVIEW)) {
            throw new SubmissionStatusChangeException(String.format("You can request changes for submissions only when they " +
                            "are in state %s. Current submission state is %s", WAITING_FOR_REVIEW.getStatus(),
                    submission.getSubmissionStatusTypeEnum().getStatus()));
        }

        submission = saveSubmissionWithNewType(submission, CHANGES_REQUESTED);
        createComment(submissionRequestChangesDto, submission);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    @PostMapping(path = "/{id}/comments")
    public ResponseEntity<Void> commentSubmission(@PathVariable Long id, @Valid @RequestBody SubmissionCommentDto submissionCommentDto) {
        Submission submission = submissionService.getSubmissionById(id);
        createComment(submissionCommentDto, submission);

        return ResponseEntity.ok().build();
    }

    private void createComment(Commentable commentable, Submission submission) {
        try {
            commentService.createComment(commentable, submission);
        } catch (NotSuchCommentTypeSpecifiedException ex) {
            log.error(ex.getMessage(), ex);
            throw new InternalServerErrorException("Could not create a comment for a reopen. Please contect wit a administrator");
        }
    }

    private Submission saveSubmissionWithNewType(Submission submission, SubmissionStatusTypeEnum submissionStatusTypeEnum) {
        SubmissionStatusType submissionStatusType = new SubmissionStatusType();
        submissionStatusType.setName(submissionStatusTypeEnum.getStatus());
        submission.setSubmissionStatusType(submissionStatusType);

        return submissionService.updateSubmission(submission);
    }
}
