package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.SubmissionNotFoundException;
import pl.coddlers.core.models.converters.SubmissionConverter;
import pl.coddlers.core.models.dto.SubmissionDto;
import pl.coddlers.core.models.entity.*;
import pl.coddlers.core.repositories.SubmissionRepository;

import java.util.Collection;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final SubmissionConverter submissionConverter;

    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository, SubmissionConverter submissionConverter) {
        this.submissionRepository = submissionRepository;
        this.submissionConverter = submissionConverter;
    }

    public Collection<SubmissionDto> getAllTaskSubmissions(long taskId) {
        return submissionConverter.convertFromEntities(submissionRepository.findByTaskId(taskId));
    }

    public SubmissionDto getSubmissionByBranchNameAndRepoName(String branchName, String repoName) {
        return submissionConverter.convertFromEntity(
                submissionRepository.findByBranchNameAndStudentLessonRepository_RepositoryUrl(branchName, repoName)
                        .orElseThrow(() -> new SubmissionNotFoundException(branchName, repoName))
        );
    }

    public void updateSubmission(SubmissionDto submissionDto) {
        if (submissionDto.getId() == null || !submissionRepository.existsById(submissionDto.getId())) {
            throw new SubmissionNotFoundException(submissionDto.getId());
        }
        submissionRepository.save(submissionConverter.convertFromDto(submissionDto));
    }

    public Submission updateSubmission(Submission submission) {
        if (submission.getId() == null || !submissionRepository.existsById(submission.getId())) {
            throw new SubmissionNotFoundException(submission.getId());
        }
        return submissionRepository.save(submission);
    }


    public Submission createSubmission(SubmissionDto submissionDto) {
        return submissionRepository.save(submissionConverter.convertFromDto(submissionDto));
    }

    public void createSubmission(CourseEdition courseEdition, Task task, User user, StudentLessonRepository studentLessonRepository) {
        Submission submission = new Submission();
        SubmissionStatusType submissionStatusType = new SubmissionStatusType();
        submissionStatusType.setName(SubmissionStatusTypeEnum.NOT_SUBMITTED.getStatus());
        submission.setSubmissionStatusType(submissionStatusType);
        submission.setUser(user);
        submission.setCourseEdition(courseEdition);
        submission.setStudentLessonRepository(studentLessonRepository);
        submission.setTask(task);
        submission.setBranchName(task.getBranchNamePrefix());
        submissionRepository.saveAndFlush(submission);
    }

    public int countAllSubmittedTasks(User user, CourseEdition courseEdition) {
        return submissionRepository.countAllByUserAndCourseEditionAndSubmissionStatusTypeName(user,
                courseEdition,
                SubmissionStatusTypeEnum.WAITING_FOR_REVIEW.getStatus()) +
                countAllGradedTasks(user, courseEdition);
    }

    public int countAllGradedTasks(User user, CourseEdition courseEdition) {
        return submissionRepository.countAllByUserAndCourseEditionAndSubmissionStatusTypeName(user,
                courseEdition,
                SubmissionStatusTypeEnum.GRADED.getStatus());
    }

    public int countAllTask(User user, CourseEdition courseEdition) {
        return submissionRepository.countAllByUserAndCourseEdition(user, courseEdition);
    }

    public Submission getSubmissionById(Long id) {
        return submissionRepository.findById(id).orElseThrow(() -> new SubmissionNotFoundException(id));
    }
}
