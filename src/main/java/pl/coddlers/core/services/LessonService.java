package pl.coddlers.core.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.*;
import pl.coddlers.core.models.converters.LessonConverter;
import pl.coddlers.core.models.dto.LessonDto;
import pl.coddlers.core.models.entity.*;
import pl.coddlers.core.repositories.*;
import pl.coddlers.git.models.event.ProjectDto;
import pl.coddlers.git.services.GitLessonService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LessonService {
    private final UserDetailsServiceImpl userDetailsService;
    private final LessonRepository lessonRepository;
    private final LessonConverter lessonConverter;
    private final CourseVersionRepository courseVersionRepository;
    private final GitLessonService gitLessonService;
    private final UserDataRepository userDataRepository;
    private final CourseRepository courseRepository;
    private final SubmissionService submissionService;
    private final CourseEditionRepository courseEditionRepository;
    private final StudentLessonRepositoryRepository studentLessonRepositoryRepository;
    private final TaskRepository taskRepository;
    private final CourseEditionLessonService courseEditionLessonService;
    private final CourseEditionLessonRepository courseEditionLessonRepository;

    @Value("${gitlab.api.host}:${gitlab.api.http.port}/")
    private String gitlabUrl;


    @Autowired
    public LessonService(UserDetailsServiceImpl userDetailsService, LessonRepository lessonRepository,
                         LessonConverter lessonConverter, CourseVersionRepository courseVersionRepository,
                         GitLessonService gitLessonService, UserDataRepository userDataRepository,
                         CourseRepository courseRepository, SubmissionService submissionService,
                         CourseEditionRepository courseEditionRepository,
                         StudentLessonRepositoryRepository studentLessonRepositoryRepository,
                         TaskRepository taskRepository, CourseEditionLessonService courseEditionLessonService,
                         CourseEditionLessonRepository courseEditionLessonRepository) {
        this.userDetailsService = userDetailsService;
        this.lessonRepository = lessonRepository;
        this.lessonConverter = lessonConverter;
        this.courseVersionRepository = courseVersionRepository;
        this.gitLessonService = gitLessonService;
        this.userDataRepository = userDataRepository;
        this.courseRepository = courseRepository;
        this.submissionService = submissionService;
        this.courseEditionRepository = courseEditionRepository;
        this.studentLessonRepositoryRepository = studentLessonRepositoryRepository;
        this.taskRepository = taskRepository;
        this.courseEditionLessonService = courseEditionLessonService;
        this.courseEditionLessonRepository = courseEditionLessonRepository;
    }

    public Collection<LessonDto> getAllCourseVersionLessons(Long courseId, Integer courseVersionNumber) {
        CourseVersion courseVersion;

        if (courseVersionNumber == null) {
            List<CourseVersion> courseVersionList = courseVersionRepository.findByCourse_IdOrderByVersionNumberDesc(courseId);
            try {
                courseVersion = courseVersionList.get(0);
            } catch (IndexOutOfBoundsException e) {
                throw new CourseVersionNotFound(courseId, 1);
            }
        } else {
            courseVersion = courseVersionRepository.findByCourse_IdAndVersionNumber(courseId, courseVersionNumber)
                    .orElseThrow(() -> new CourseVersionNotFound(courseId, courseVersionNumber));
        }

        return getAllCourseVersionLessons(courseVersion.getId());
    }

    public Collection<LessonDto> getLessonsByCourseEditionId(Long courseEditionId) {
        return lessonConverter.convertFromEntities(lessonRepository.getLessonsByCourseEditionIdOrderByStartDate(courseEditionId));
    }

    private Collection<LessonDto> getAllCourseVersionLessons(long courseVersionId) {
        return lessonConverter.convertFromEntities(lessonRepository.findByCourseVersionId(courseVersionId));
    }

    public Long createLesson(LessonDto lessonDto) {
        try {
            Lesson lesson = lessonConverter.convertFromDto(lessonDto);
            lessonRepository.save(lesson);
            User currentUser = userDetailsService.getCurrentUserEntity();
            Course lessonCourse = getLessonCourse(lesson);
            if (lessonCourse.getGitGroupId() == null) {
                log.error(String.format("GitGroupId is null. Git group for course %s (id=%d) probably doesn't exists.", lessonCourse.getTitle(), lessonCourse.getId()));
                throw new InternalServerErrorException(String.format("There was a internal problem while creating a lesson"));
            }
            CompletableFuture<ProjectDto> gitLessonIdFuture = gitLessonService.createLesson(currentUser.getGitUserId(), createRepositoryName(lesson))
                    .thenCompose(projectDto -> gitLessonService.transferRepositoryToGroup(projectDto.getId(), lessonCourse.getGitGroupId()));
            ProjectDto projectDto = gitLessonIdFuture.get();
            lesson.setGitProjectId(projectDto.getId());
            lesson.setRepositoryName(projectDto.getPathWithNamespace());
            lessonRepository.save(lesson);

            Collection<CourseEdition> courseEditions = courseEditionRepository.findAllByCourseVersionId(lesson.getCourseVersion().getId());
            courseEditions.forEach(courseEdition -> {
                Optional<CourseEditionLesson> lastCourseEditionLessonOpt = courseEditionLessonRepository
                        .findFirstByCourseEdition_IdOrderByStartDateDesc(courseEdition.getId());
                Timestamp startDate = courseEdition.getStartDate();
                if (lastCourseEditionLessonOpt.isPresent()) {
                    startDate = courseEditionLessonService.addDaysToDate(lastCourseEditionLessonOpt.get().getEndDate(), 1);
                }
                courseEditionLessonService.createCourseEditionLesson(courseEdition, lesson, startDate);
            });

            return lesson.getId();
        } catch (InternalServerErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Exception while creating lesson for: " + lessonDto.toString(), ex);
            throw new LessonAlreadyExists(ex.getMessage());
        }
    }

    private Course getLessonCourse(Lesson lesson) {
        Optional<Course> byCourseVersionId = courseRepository.findByCourseVersionId(lesson.getCourseVersion().getId());
        if (!byCourseVersionId.isPresent()) {
            throw new IllegalArgumentException("Course not found");
        }
        return byCourseVersionId.get();
    }

    public CompletableFuture<Lesson> createNewVersionLesson(Lesson modelLesson, CourseVersion newCourseVersion) {
        try {
            Lesson lesson = lessonRepository.save(copyLessonEntity(modelLesson, newCourseVersion));
            Collection<Task> tasks = taskRepository.findByLessonId(modelLesson.getId());
            Course lessonCourse = getLessonCourse(modelLesson);
            User currentUser = userDetailsService.getCurrentUserEntity();
            return gitLessonService.forkLesson(modelLesson, currentUser)
                    .thenApply(projectDto -> gitLessonService.renameForkedRepository(projectDto.getId(), createRepositoryName(lesson)))
                    .thenCompose(projectDto -> gitLessonService.transferRepositoryToGroup(projectDto.getId(), lessonCourse.getGitGroupId()))
                    .thenApply(projectDto -> {
                        lesson.setGitProjectId(projectDto.getId());
                        lesson.setRepositoryName(projectDto.getPathWithNamespace());
                        Lesson savedLesson = lessonRepository.save(lesson);
                        tasks.forEach(t -> {
                            Task task = new Task();
                            task.setTitle(t.getTitle());
                            task.setDescription(t.getDescription());
                            task.setMaxPoints(t.getMaxPoints());
                            task.setIsCodeTask(t.getIsCodeTask());
                            task.setBranchNamePrefix(t.getBranchNamePrefix());
                            task.setLesson(savedLesson);
                            //todo task.set task.setNotes();
                            taskRepository.save(task);
                        });
                        return savedLesson;
                    });
        } catch (Exception ex) {
            log.error("Exception while creating new version lesson for: " + modelLesson.toString(), ex);
            throw new LessonAlreadyExists(ex.getMessage());
        }
    }

    private Lesson copyLessonEntity(Lesson modelLesson, CourseVersion courseVersion) {
        Lesson lesson = new Lesson();
        lesson.setTitle(modelLesson.getTitle());
        lesson.setDescription(modelLesson.getDescription());
        lesson.setWeight(modelLesson.getWeight());
        lesson.setTimeInDays(modelLesson.getTimeInDays());
        lesson.setTitle(modelLesson.getTitle());
        lesson.setCourseVersion(courseVersion);
        return lesson;
    }


    public CompletableFuture<StudentLessonRepository> forkModelLessonForUser(CourseEdition courseEdition, Lesson lesson, User user) {
        return forkLessonForUser(courseEdition, lesson, user);
    }

    public CompletableFuture<List<StudentLessonRepository>> forkModelLesson(CourseEdition courseEdition, Lesson lesson) {
        List<CompletableFuture<StudentLessonRepository>> listOfCompletableStudentRepositories = cloneLessonForUsersEnrolledToEdition(courseEdition, lesson);
        return mapListOfFuturesToFutureOfList(listOfCompletableStudentRepositories);
    }

    private List<CompletableFuture<StudentLessonRepository>> cloneLessonForUsersEnrolledToEdition(CourseEdition courseEdition, Lesson lesson) {
        return userDataRepository.getCourseEditionUsers(courseEdition.getId()).stream()
                .map(user -> forkLessonForUser(courseEdition, lesson, user))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private CompletableFuture<StudentLessonRepository> forkLessonForUser(CourseEdition courseEdition, Lesson lesson, User user) {
        return gitLessonService.forkLesson(lesson, user)
                .thenApply(projectDto -> gitLessonService.renameForkedRepository(projectDto.getId(), buildForkedRepositoryName(courseEdition.getId(), lesson.getId(), user.getId())))
                .thenCompose(projectDto -> gitLessonService.transferRepositoryToGroup(projectDto.getId(), courseEdition.getGitGroupId()))
                .thenApply(projectDto -> gitLessonService.createStudentLessonRepository(courseEdition, user, lesson, projectDto))
                .thenApply(studentLessonRepository -> {
                    studentLessonRepositoryRepository.save(studentLessonRepository);
                    taskRepository.findByLessonId(lesson.getId()).
                            forEach(task -> submissionService.createSubmission(courseEdition, task, user, studentLessonRepository));
                    return studentLessonRepository;
                });
    }

    private String buildForkedRepositoryName(Long courseEditionId, Long lessonId, Long studentId) {
        String timestamp = Long.toString(Instant.now().getEpochSecond());
        return courseEditionId + "_" + lessonId + "_" + studentId + "_" + timestamp;
    }

    private CompletableFuture<List<StudentLessonRepository>> mapListOfFuturesToFutureOfList(List<CompletableFuture<StudentLessonRepository>> listOfFutureRepositories) {
        return CompletableFuture.allOf(listOfFutureRepositories.toArray(new CompletableFuture[listOfFutureRepositories.size()]))
                .thenApply(v -> listOfFutureRepositories.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
                );
    }

    private String createRepositoryName(Lesson lesson) {
        String timestamp = Long.toString(Instant.now().getEpochSecond());
        return lesson.getCourseVersion().getCourse().getId() + "_" + lesson.getCourseVersion().getId() + "_" + timestamp;
    }

    public LessonDto getLessonById(Long id) {
        Lesson lesson = validateLesson(id);

        return lessonConverter.convertFromEntity(lesson);
    }

    public LessonDto updateLesson(Long id, LessonDto lessonDto) {
        validateLesson(id);
        lessonDto.setId(id);
        Lesson lesson = lessonConverter.convertFromDto(lessonDto);
        lessonRepository.save(lesson);

        return lessonDto;
    }

    private Lesson validateLesson(Long id) throws LessonNotFoundException {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new LessonNotFoundException(id));
    }

    public List<StudentLessonRepository> forkLessons(Long courseEditionId, Long lessonId) {
        // TODO: 14.11.18 remove me
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new LessonNotFoundException(lessonId));
        CourseEdition courseEdition = courseEditionRepository.findById(courseEditionId).orElseThrow(() -> new CourseEditionNotFoundException(courseEditionId));
        try {
            return forkModelLesson(courseEdition, lesson)
                    .whenComplete(((studentLessonRepositories, throwable) -> {
//                        studentLessonRepositoryRepository.saveAll(studentLessonRepositories);
                        log.info(String.format("Created %d repositories", studentLessonRepositories.size()));
                    }))
                    .exceptionally(ex -> {
                        log.error(String.format("Error while forking lesson with id %d for courseEdition with id %d", lesson.getId(), courseEdition.getId()), ex);
                        return Collections.emptyList();
                    }).get();
        } catch (Exception e) {
            log.error("Cannot fork: " + courseEditionId + " " + lessonId, e);
        }
        return new LinkedList<>();
    }

    public String getLessonRepositoryUrl(Long lessonId) {
        return lessonRepository.findById(lessonId)
                .map(Lesson::getRepositoryName)
                .map(repositoryPath -> this.gitlabUrl + repositoryPath)
                .orElseThrow(() -> new LessonNotFoundException(lessonId));
    }
}
