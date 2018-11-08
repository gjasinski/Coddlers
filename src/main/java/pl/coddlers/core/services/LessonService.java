package pl.coddlers.core.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.CourseVersionNotFound;
import pl.coddlers.core.exceptions.LessonAlreadyExists;
import pl.coddlers.core.exceptions.LessonNotFoundException;
import pl.coddlers.core.models.converters.LessonConverter;
import pl.coddlers.core.models.dto.LessonDto;
import pl.coddlers.core.models.entity.*;
import pl.coddlers.core.repositories.CourseRepository;
import pl.coddlers.core.repositories.CourseVersionRepository;
import pl.coddlers.core.repositories.LessonRepository;
import pl.coddlers.core.repositories.UserDataRepository;
import pl.coddlers.git.models.event.ProjectDto;
import pl.coddlers.git.services.GitLessonService;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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


    @Autowired
    public LessonService(UserDetailsServiceImpl userDetailsService, LessonRepository lessonRepository,
                         LessonConverter lessonConverter, CourseVersionRepository courseVersionRepository,
                         GitLessonService gitLessonService, UserDataRepository userDataRepository,
                         CourseRepository courseRepository, SubmissionService submissionService) {
        this.userDetailsService = userDetailsService;
        this.lessonRepository = lessonRepository;
        this.lessonConverter = lessonConverter;
        this.courseVersionRepository = courseVersionRepository;
        this.gitLessonService = gitLessonService;
        this.userDataRepository = userDataRepository;
        this.courseRepository = courseRepository;
        this.submissionService = submissionService;
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
            Course byCourseVersionId = getLessonCourse(lesson);
            CompletableFuture<ProjectDto> gitLessonIdFuture = gitLessonService.createLesson(currentUser.getGitUserId(), createRepositoryName(lesson))
                    .thenCompose(projectDto -> gitLessonService.transferRepositoryToGroup(projectDto.getId(), byCourseVersionId.getGitGroupId()));
            ProjectDto projectDto = gitLessonIdFuture.get();
            lesson.setGitProjectId(projectDto.getId());
            lesson.setRepositoryUrl(projectDto.getPathWithNamespace());
            lessonRepository.save(lesson);
            return lesson.getId();
        } catch (Exception ex) {
            log.error("Exception while creating lesson for: " + lessonDto.toString(), ex);
            throw new LessonAlreadyExists(ex.getMessage());
        }
    }

    private Course getLessonCourse(Lesson lesson) {
        Optional<Course> byCourseVersionId = courseRepository.findByCourseVersion_Id(lesson.getCourseVersion().getId());
        if (!byCourseVersionId.isPresent()) {
            throw new IllegalArgumentException("Course not found");
        }
        return byCourseVersionId.get();
    }

    public CompletableFuture<Lesson> createNewVersionLesson(Lesson modelLesson, CourseVersion newCourseVersion) {
        try {
            Lesson lesson = lessonRepository.save(copyLessonEntity(modelLesson, newCourseVersion));
            Course lessonCourse = getLessonCourse(modelLesson);
            User currentUser = userDetailsService.getCurrentUserEntity();
            return gitLessonService.forkLesson(modelLesson, currentUser)
                    .thenApply(projectDto -> gitLessonService.renameForkedRepository(projectDto.getId(), createRepositoryName(lesson)))
                    .thenCompose(projectDto -> gitLessonService.transferRepositoryToGroup(projectDto.getId(), lessonCourse.getGitGroupId()))
                    .thenApply(projectDto -> {
                        lesson.setGitProjectId(projectDto.getId());
                        lesson.setRepositoryUrl(projectDto.getPathWithNamespace());
                        return lessonRepository.save(lesson);
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
                    lesson.getTasks().forEach(task -> submissionService.createSubmission(courseEdition, task, user, studentLessonRepository));
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
}
