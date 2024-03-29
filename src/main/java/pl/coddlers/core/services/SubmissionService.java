package pl.coddlers.core.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.GitAsynchronousOperationException;
import pl.coddlers.core.exceptions.StudentLessonRepositoryNotFoundException;
import pl.coddlers.core.exceptions.SubmissionNotFoundException;
import pl.coddlers.core.exceptions.TaskNotFoundException;
import pl.coddlers.core.exceptions.UserNotFoundException;
import pl.coddlers.commons.Tuple;
import pl.coddlers.core.models.converters.SubmissionConverter;
import pl.coddlers.core.models.dto.GitFileContentDto;
import pl.coddlers.core.models.dto.SubmissionDto;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.StudentLessonRepository;
import pl.coddlers.core.models.entity.Submission;
import pl.coddlers.core.models.entity.SubmissionStatusType;
import pl.coddlers.core.models.entity.SubmissionStatusTypeEnum;
import pl.coddlers.core.models.entity.Task;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.core.repositories.*;
import pl.coddlers.git.models.GitFileDto;
import pl.coddlers.git.services.GitFileService;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SubmissionService {

    public static final String BLOB = "blob";
    public static final String MASTER_BRANCH_SUFFIX = "-master";
    private final SubmissionRepository submissionRepository;
    private final SubmissionConverter submissionConverter;
    private final CourseEditionRepository courseEditionRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final TaskRepository taskRepository;
    private final GitFileService gitFileService;
    private final StudentLessonRepositoryRepository studentLessonRepositoryRepository;
    private final UserDataRepository userDataRepository;
    private final LessonRepository lessonRepository;

    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository, SubmissionConverter submissionConverter, CourseEditionRepository courseEditionRepository, UserDetailsServiceImpl userDetailsService, TaskRepository taskRepository, GitFileService gitFileService, StudentLessonRepositoryRepository studentLessonRepositoryRepository, UserDataRepository userDataRepository, LessonRepository lessonRepository) {
        this.submissionRepository = submissionRepository;
        this.submissionConverter = submissionConverter;
        this.courseEditionRepository = courseEditionRepository;
        this.userDetailsService = userDetailsService;
        this.taskRepository = taskRepository;
        this.gitFileService = gitFileService;
        this.studentLessonRepositoryRepository = studentLessonRepositoryRepository;
        this.userDataRepository = userDataRepository;
        this.lessonRepository = lessonRepository;
    }

    public Collection<SubmissionDto> getAllTaskSubmissions(long taskId) {
        return submissionConverter.convertFromEntities(submissionRepository.findByTaskId(taskId));
    }

    public Collection<SubmissionDto> getAllTaskSubmissionsForCourseEdition(long taskId, long courseEditionId) {
        return submissionConverter.convertFromEntities(submissionRepository.findByTaskIdAndCourseEditionId(taskId, courseEditionId));
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

    public int countGradedLessonStatus(User user, CourseEdition courseEdition) {
        return lessonRepository.getLessonsByCourseEditionIdOrderByStartDate(courseEdition.getId())
                .stream()
                .map(lesson -> {
                    Collection<Submission> submissions = submissionRepository.findSubmissionForTaskAndUser(lesson.getId(), user.getId(), courseEdition);
                    boolean graded = submissions.size() != 0 && submissions.stream()
                            .filter(submission -> submission.getSubmissionStatusTypeEnum() == SubmissionStatusTypeEnum.GRADED)
                            .collect(Collectors.toList()).size() == submissions.size();

                    return graded ? 1 : 0;
                })
                .reduce((a, b) -> a + b)
                .orElse(0);
    }

    public int countSubmittedLessonStatus(User user, CourseEdition courseEdition) {
        return lessonRepository.getLessonsByCourseEditionIdOrderByStartDate(courseEdition.getId())
                .stream()
                .map(lesson -> {
                    Collection<Submission> submissions = submissionRepository.findSubmissionForTaskAndUser(lesson.getId(), user.getId(), courseEdition);
                    boolean submitted = submissions.size() != 0 && (submissions.stream()
                            .filter(submission -> submission.getSubmissionStatusTypeEnum() == SubmissionStatusTypeEnum.WAITING_FOR_REVIEW ||
                                    submission.getSubmissionStatusTypeEnum() == SubmissionStatusTypeEnum.GRADED)
                            .collect(Collectors.toList()).size() == submissions.size());

                    return submitted ? 1 : 0;
                })
                .reduce((a, b) -> a + b)
                .orElse(0);
    }

    public Submission getSubmissionById(Long id) {
        return submissionRepository.findById(id).orElseThrow(() -> new SubmissionNotFoundException(id));
    }

    public Collection<SubmissionDto> getTaskSubmission(Long lessonId, Long courseEditionId) {
        User currentUser = userDetailsService.getCurrentUserEntity();
        CourseEdition courseEdition = courseEditionRepository.getOne(courseEditionId);
        return submissionConverter.convertFromEntities(submissionRepository.findSubmissionForTaskAndUser(lessonId, currentUser.getId(), courseEdition));
    }

    public Collection<GitFileContentDto> getSubmissionContent(Long submissionId) {
        Submission submission = this.submissionRepository.findById(submissionId)
                .orElseThrow(() -> new SubmissionNotFoundException(submissionId));
        long taskId = submission.getTask().getId();
        Task task = this.taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        Long studentLessonRepositoryId = submission.getStudentLessonRepository().getId();
        StudentLessonRepository studentLessonRepository = studentLessonRepositoryRepository
                .findById(studentLessonRepositoryId)
                .orElseThrow(() -> new StudentLessonRepositoryNotFoundException(studentLessonRepositoryId));
        String branchName = task.getBranchNamePrefix() + MASTER_BRANCH_SUFFIX;

        CompletableFuture<List<GitFileContentDto>> listCompletableFuture = this.gitFileService
                .getRepositoryFiles(studentLessonRepository.getGitRepositoryId(), branchName)
                .thenApply((GitFileDto[] repoFileArray) -> new LinkedList<>(Arrays.asList(repoFileArray)))
                .thenApply(downloadAllFiles(submissionId, taskId, studentLessonRepository, branchName)
                );
        try {
            return listCompletableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            String message = String.format("Cannot download content of file. SubmissionId: %s, taskId: %s," +
                            " gitRepositoryId: %s, branchName: %s.", submissionId, taskId,
                    studentLessonRepository.getGitRepositoryId(), branchName);
            log.error(message, e);
            throw new GitAsynchronousOperationException(message);
        }
    }

    private Function<LinkedList<GitFileDto>, List<GitFileContentDto>> downloadAllFiles(Long submissionId,
                                                                                       long taskId,
                                                                                       StudentLessonRepository studentLessonRepository,
                                                                                       String branchName) {
        return (LinkedList<GitFileDto> listOfGitFiles) -> listOfGitFiles.stream()
                .filter(filterOnlyFiles())
                .map((GitFileDto gitFile) -> {
                    CompletableFuture<String> contentFuture = downloadFile(submissionId, taskId, studentLessonRepository, branchName, gitFile);
                    return new Tuple<>(gitFile, contentFuture);
                })
                .map(getContentFromFutureAndCreateGitFileContent(submissionId, taskId, studentLessonRepository, branchName))
                .collect(Collectors.toList());
    }

    private Predicate<GitFileDto> filterOnlyFiles() {
        return (GitFileDto gitFile) -> gitFile.getType().equals(BLOB);
    }

    private CompletableFuture<String> downloadFile(Long submissionId,
                                                   long taskId,
                                                   StudentLessonRepository studentLessonRepository,
                                                   String branchName,
                                                   GitFileDto gitFile) {
        return this.gitFileService
                .getFileContent(studentLessonRepository.getGitRepositoryId(), branchName, gitFile.getPath())
                .handle((String s, Throwable t) -> {
                    if (t != null) {
                        String message = String.format("Cannot download content of file %s. " +
                                        "SubmissionId: %s, " +
                                        "taskId: %s, " +
                                        "gitRepositoryId: %s, " +
                                        "branchName: %s.",
                                gitFile,
                                submissionId,
                                taskId,
                                studentLessonRepository.getGitRepositoryId(),
                                branchName);
                        log.error(message + t.getMessage(), t);
                        return null;
                    } else {
                        return s;
                    }
                });
    }

    private Function<Tuple<GitFileDto, CompletableFuture<String>>, GitFileContentDto> getContentFromFutureAndCreateGitFileContent(Long submissionId,
                                                                                                                                  long taskId,
                                                                                                                                  StudentLessonRepository studentLessonRepository,
                                                                                                                                  String branchName) {
        return (Tuple<GitFileDto, CompletableFuture<String>> tuple) -> {
            try {
                GitFileContentDto fileContent = new GitFileContentDto();
                fileContent.setPath(tuple.getKey().getPath());
                fileContent.setContent(tuple.getValue().get());
                return fileContent;
            } catch (InterruptedException | ExecutionException e) {
                String message = String.format("Cannot download content of file. " +
                                "SubmissionId: %s, " +
                                "taskId: %s, " +
                                "gitRepositoryId: %s, " +
                                "branchName: %s, " +
                                "file path %s",
                        submissionId,
                        taskId,
                        studentLessonRepository.getGitRepositoryId(),
                        branchName,
                        tuple.getKey().getPath());
                log.error(message, e);
                return null;
            }
        };
    }

    public String getStudentFullName(Long submissionId) {
        Submission submission = this.submissionRepository.findById(submissionId)
                .orElseThrow(() -> new SubmissionNotFoundException(submissionId));
        Long id = submission.getUser().getId();
        User user = userDataRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return user.getFullName();
    }
}

