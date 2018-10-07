package pl.coddlers.core.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.CourseVersionNotFound;
import pl.coddlers.core.exceptions.LessonAlreadyExists;
import pl.coddlers.core.exceptions.LessonNotFoundException;
import pl.coddlers.core.models.converters.LessonConverter;
import pl.coddlers.core.models.dto.LessonDto;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.CourseVersion;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.models.entity.StudentLessonRepository;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.core.repositories.CourseVersionRepository;
import pl.coddlers.core.repositories.LessonRepository;
import pl.coddlers.core.repositories.UserDataRepository;
import pl.coddlers.git.models.event.ProjectDto;
import pl.coddlers.git.services.GitLessonService;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LessonService {
    private static final String DATE_FORMAT = "ddMMyyyyhhmmss";
    private final UserDetailsServiceImpl userDetailsService;
    private final LessonRepository lessonRepository;
    private final LessonConverter lessonConverter;
    private final CourseVersionRepository courseVersionRepository;
    private final GitLessonService gitProjectService;
    private final UserDataRepository userDataRepository;


    @Autowired
    public LessonService(UserDetailsServiceImpl userDetailsService, LessonRepository lessonRepository, LessonConverter lessonConverter,
                         CourseVersionRepository courseVersionRepository, GitLessonService gitProjectService, UserDataRepository userDataRepository) {
        this.userDetailsService = userDetailsService;
        this.lessonRepository = lessonRepository;
        this.lessonConverter = lessonConverter;
        this.courseVersionRepository = courseVersionRepository;
        this.gitProjectService = gitProjectService;
        this.userDataRepository = userDataRepository;
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

    private Collection<LessonDto> getAllCourseVersionLessons(long courseVersionId) {
        return lessonConverter.convertFromEntities(lessonRepository.findByCourseVersionId(courseVersionId));
    }

    public Long createLesson(LessonDto lessonDto) {
        try {
            Lesson lesson = lessonConverter.convertFromDto(lessonDto);
            lessonRepository.save(lesson);
            User currentUser = userDetailsService.getCurrentUserEntity();
            CompletableFuture<ProjectDto> gitLessonIdFuture = gitProjectService.createLesson(currentUser.getGitUserId(), createRepositoryName(lesson));
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
        try {
            return gitProjectService.forkLesson(lesson, user, courseEdition);
        } catch (Exception ex) {
            // TODO: 07.10.18 exception logic
            return null;
        }
    }

    private CompletableFuture<List<StudentLessonRepository>> mapListOfFuturesToFutureOfList(List<CompletableFuture<StudentLessonRepository>> listOfFutureRepositories) {
        return CompletableFuture.allOf(listOfFutureRepositories.toArray(new CompletableFuture[listOfFutureRepositories.size()]))
                .thenApply(v -> listOfFutureRepositories.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
                );
    }

    private String createRepositoryName(Lesson lesson) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String date = dateFormat.format(new Date());
        return lesson.getCourseVersion().getCourse().getId() + "_" + lesson.getCourseVersion().getId() + "_" + lesson.getId() + "_" + date;
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
