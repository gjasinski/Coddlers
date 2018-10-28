package pl.coddlers.core.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.CourseEditionNotFoundException;
import pl.coddlers.core.exceptions.GitAsynchronousOperationException;
import pl.coddlers.core.models.converters.CourseEditionConverter;
import pl.coddlers.core.models.dto.CourseDto;
import pl.coddlers.core.models.dto.CourseEditionDto;
import pl.coddlers.core.models.dto.CourseWithCourseEditionDto;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.CourseEditionLesson;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.core.repositories.CourseEditionLessonRepository;
import pl.coddlers.core.repositories.CourseEditionRepository;
import pl.coddlers.core.repositories.CourseRepository;
import pl.coddlers.core.repositories.LessonRepository;
import pl.coddlers.core.repositories.UserDataRepository;
import pl.coddlers.git.services.GitGroupService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseEditionService {
    private final CourseEditionConverter courseEditionConverter;
    private final CourseEditionRepository courseEditionRepository;
    private final CourseEditionLessonRepository courseEditionLessonRepository;
    private final LessonRepository lessonRepository;
    private final SubmissionService submissionService;
    private final CourseService courseService;
    private final GitGroupService gitGroupService;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public CourseEditionService(CourseEditionRepository courseEditionRepository, CourseEditionConverter courseEditionConverter,
                                LessonRepository lessonRepository, CourseEditionLessonRepository courseEditionLessonRepository,
                                GitGroupService gitGroupService, UserDetailsServiceImpl userDetailsService,
                                SubmissionService submissionService, CourseService courseService) {
        this.courseEditionRepository = courseEditionRepository;
        this.courseEditionConverter = courseEditionConverter;
        this.lessonRepository = lessonRepository;
        this.courseEditionLessonRepository = courseEditionLessonRepository;
        this.submissionService = submissionService;
        this.courseService = courseService;
        this.gitGroupService = gitGroupService;
        this.userDetailsService = userDetailsService;
    }

    public CourseEditionDto getCourseEditionById(Long id) {
        CourseEdition courseEdition = validateCourseEdition(id);
        return courseEditionConverter.convertFromEntity(courseEdition);
    }

    private CourseEdition validateCourseEdition(Long id) throws CourseEditionNotFoundException {
        return courseEditionRepository.findById(id).orElseThrow(() -> new CourseEditionNotFoundException(id));
    }

    public List<CourseEditionDto> getCourseEditionsByCourseVersionId(Long courseVersionId) {
        return courseEditionRepository.findAllByCourseVersionId(courseVersionId).stream()
                .map(courseEditionConverter::convertFromEntity)
                .collect(Collectors.toList());
    }

    public CourseEdition createCourseEdition(CourseEditionDto courseEditionDto) {
        try {
            CourseEdition courseEdition = courseEditionRepository.save(courseEditionConverter.convertFromDto(courseEditionDto));
            Long gitGroupId = gitGroupService.createGroup(createGroupName(courseEditionDto, courseEdition.getId())).get().getId();
            courseEdition.setGitGroupId(gitGroupId);
            courseEditionRepository.save(courseEdition);
            addTeachersToGroup(gitGroupId);
            return courseEdition;
        } catch (InterruptedException | ExecutionException e) {
            log.error(String.format("Cannot create new course edition for %s", courseEditionDto.toString()), e);
            throw new GitAsynchronousOperationException("Cannot create new course edition");
        }
    }

    private void addTeachersToGroup(Long gitGroupId) {
        User currentUser = userDetailsService.getCurrentUserEntity();
        gitGroupService.addUserToGroupAsMaintainer(currentUser.getGitUserId(), gitGroupId);
        // TODO: 20.10.18 add all teacher to group from database
    }

    private String createGroupName(CourseEditionDto courseEditionDto, Long courseEditionId) {
        Long courseVersionId = courseEditionDto.getCourseVersion().getId();
        return courseVersionId + "_" + courseEditionId + "_" + Long.toString(Instant.now().getEpochSecond());
    }

    public List<CourseEditionLesson> createCourseEditionLessons(CourseEdition courseEdition) {
        List<Lesson> lessons = lessonRepository.findByCourseVersionId(courseEdition.getCourseVersion().getId());
        return lessons.stream()
                .map(lesson -> {
                    CourseEditionLesson courseEditionLesson = createCourseEditionLesson(courseEdition, lesson);
                    return courseEditionLessonRepository.save(courseEditionLesson);
                })
                .collect(Collectors.toList());
    }

    private CourseEditionLesson createCourseEditionLesson(CourseEdition courseEdition, Lesson lesson) {
        CourseEditionLesson courseEditionLesson = new CourseEditionLesson();
        courseEditionLesson.setCourseEdition(courseEdition);
        courseEditionLesson.setLesson(lesson);
        courseEditionLesson.setStartDate(courseEdition.getStartDate());
        courseEditionLesson.setEndDate(getEndDate(courseEdition, lesson));
        return courseEditionLesson;
    }

    private Timestamp getEndDate(CourseEdition courseEdition, Lesson lesson) {
        LocalDateTime startDate = courseEdition.getStartDate().toLocalDateTime();
        LocalDateTime endTime = startDate.plusDays(lesson.getTimeInDays());
        return Timestamp.valueOf(endTime);
    }

    public List<CourseWithCourseEditionDto> getAllEditionsWithEnrolledStudent(User user) {
        return courseEditionRepository.findAllCourseEditionWithEnrolledStudent(user.getId())
                .stream()
                .map(courseEditionConverter::convertFromEntity)
                .map(mapEditionToCourseWithCourseEditionDto(user))
                .collect(Collectors.toList());
    }

    private Function<CourseEditionDto, CourseWithCourseEditionDto> mapEditionToCourseWithCourseEditionDto(User currentUser) {
        return edition -> {
            Long courseVersionId = edition.getCourseVersion().getId();
            CourseDto courseDto = courseService.getCourseByCourseVersionId(courseVersionId)
                    .orElseThrow(() -> new IllegalStateException(exceptionMessage(edition)));
            CourseEdition courseEdition = courseEditionRepository.getOne(edition.getId());
            int allTasks = submissionService.countAllTask(currentUser, courseEdition);
            int gradedTasks = submissionService.countAllGradedTasks(currentUser, courseEdition);
            int submittedTasks = submissionService.countAllSubmittedTasks(currentUser, courseEdition);
            return new CourseWithCourseEditionDto(courseDto, edition, submittedTasks, gradedTasks, allTasks);
        };
    }

    private String exceptionMessage(CourseEditionDto edition) {
        return String.format("Inconsistency on database, didn't found Course for CourseEdition: %s", edition.toString());
    }
}
