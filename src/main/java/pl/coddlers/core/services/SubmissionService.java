package pl.coddlers.core.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.GitAsynchronousOperationException;
import pl.coddlers.core.exceptions.SubmissionNotFoundException;
import pl.coddlers.core.exceptions.TaskNotFoundException;
import pl.coddlers.core.models.Tuple;
import pl.coddlers.core.models.converters.SubmissionConverter;
import pl.coddlers.core.models.dto.GitFileContent;
import pl.coddlers.core.models.dto.SubmissionDto;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.StudentLessonRepository;
import pl.coddlers.core.models.entity.Submission;
import pl.coddlers.core.models.entity.SubmissionStatusType;
import pl.coddlers.core.models.entity.SubmissionStatusTypeEnum;
import pl.coddlers.core.models.entity.Task;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.core.repositories.CourseEditionRepository;
import pl.coddlers.core.repositories.StudentLessonRepositoryRepository;
import pl.coddlers.core.repositories.SubmissionRepository;
import pl.coddlers.core.repositories.TaskRepository;
import pl.coddlers.git.models.GitFile;
import pl.coddlers.git.services.GitFileService;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final SubmissionConverter submissionConverter;
    private final CourseEditionRepository courseEditionRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final TaskRepository taskRepository;
    private final GitFileService gitFileService;
    private final StudentLessonRepositoryRepository studentLessonRepositoryRepository;

    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository, SubmissionConverter submissionConverter, CourseEditionRepository courseEditionRepository, UserDetailsServiceImpl userDetailsService, TaskRepository taskRepository, GitFileService gitFileService, StudentLessonRepositoryRepository studentLessonRepositoryRepository) {
        this.submissionRepository = submissionRepository;
        this.submissionConverter = submissionConverter;
        this.courseEditionRepository = courseEditionRepository;
        this.userDetailsService = userDetailsService;
        this.taskRepository = taskRepository;
        this.gitFileService = gitFileService;
        this.studentLessonRepositoryRepository = studentLessonRepositoryRepository;
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

    public Collection<SubmissionDto> getTaskSubmission(Long lessonId, Long courseEditionId) {
        User currentUser = userDetailsService.getCurrentUserEntity();
        CourseEdition courseEdition = courseEditionRepository.getOne(courseEditionId);
        return submissionConverter.convertFromEntities(submissionRepository.findSubmissionForTaskAndUser(lessonId, currentUser.getId(), courseEdition));
    }

    public Collection<GitFileContent> getSubmissionContent(Long submissionId) {
        Submission submission = this.submissionRepository.findById(submissionId)
                .orElseThrow(() -> new SubmissionNotFoundException(submissionId));
        long taskId = submission.getTask().getId();
        Task task = this.taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        StudentLessonRepository studentLessonRepository = studentLessonRepositoryRepository.findById(submission.getStudentLessonRepository().getId())
                .orElseThrow(() -> new IllegalStateException());
        String branchName = task.getBranchNamePrefix() + "-master";
        CompletableFuture<GitFile[]> repositoryFiles = this.gitFileService.getRepositoryFiles(studentLessonRepository.getGitRepositoryId(), branchName)
                .exceptionally(e -> {
                    String message = String.format("Cannot download list of repository files. SubmissionId: %s, taskId: %s, gitRepositoryId: %s, branchName: %s.", submissionId, taskId, studentLessonRepository.getGitRepositoryId(), branchName);
                    log.error(message, e);
                    throw new GitAsynchronousOperationException(message, e);
                });
        CompletableFuture<LinkedList<GitFile>> linkedListCompletableFuture = repositoryFiles.thenApply(repoFile -> new LinkedList<>(Arrays.asList(repoFile)));
        CompletableFuture<List<Tuple<GitFile, CompletableFuture<String>>>> blobs = linkedListCompletableFuture.thenApply(list -> list.stream()
                .filter(gitFile -> gitFile.getType().equals("blob"))
                .map(gitFile -> {
                    CompletableFuture<String> contentFuture = this.gitFileService.getFileContent(studentLessonRepository.getGitRepositoryId(), branchName, gitFile.getPath()).handle((String s, Throwable t) -> {
                        if (s == null) {
                            String message = String.format("Cannot download content of file %s. SubmissionId: %s, taskId: %s, gitRepositoryId: %s, branchName: %s.", gitFile, submissionId, taskId, studentLessonRepository.getGitRepositoryId(), branchName);
                            log.error(message, t);
                            return null;
                        } else {
                            return s;
                        }
                    });
                    return new Tuple<>(gitFile, contentFuture);
                })
                .collect(Collectors.toList()));
        CompletableFuture<List<GitFileContent>> listCompletableFuture = blobs.thenApply(listOfFuture -> listOfFuture.stream().map(f -> {
                    try {
                        GitFileContent fileContent = new GitFileContent();
                        fileContent.setPath(f.getKey().getPath());
                        fileContent.setContent(f.getValue().get());
                        log.debug(fileContent.getContent());
                        return fileContent;
                    } catch (InterruptedException | ExecutionException e) {
                        String message = String.format("Cannot download content of file. SubmissionId: %s, taskId: %s, gitRepositoryId: %s, branchName: %s.", submissionId, taskId, studentLessonRepository.getGitRepositoryId(), branchName);
                        log.error(message, e);
                        return null;
                    }
                })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );
        try {
            return listCompletableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            String message = String.format("Cannot download content of file. SubmissionId: %s, taskId: %s, gitRepositoryId: %s, branchName: %s.", submissionId, taskId, studentLessonRepository.getGitRepositoryId(), branchName);
            log.error(message, e);
            throw new GitAsynchronousOperationException(message, e);
        }
    }
}

