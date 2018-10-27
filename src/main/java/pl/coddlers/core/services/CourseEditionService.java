package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.CourseEditionLessonNotFoundException;
import pl.coddlers.core.exceptions.CourseEditionNotFoundException;
import pl.coddlers.core.models.converters.CourseEditionConverter;
import pl.coddlers.core.models.converters.CourseEditionLessonConverter;
import pl.coddlers.core.models.dto.CourseEditionDto;
import pl.coddlers.core.models.dto.CourseEditionLessonDto;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.CourseEditionLesson;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.core.repositories.CourseEditionLessonRepository;
import pl.coddlers.core.repositories.CourseEditionRepository;
import pl.coddlers.core.repositories.LessonRepository;
import pl.coddlers.git.services.GitGroupService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class CourseEditionService {
    private final CourseEditionRepository courseEditionRepository;
    private final CourseEditionConverter courseEditionConverter;
    private final LessonRepository lessonRepository;
    private final CourseEditionLessonRepository courseEditionLessonRepository;
    private final CourseEditionLessonConverter courseEditionLessonConverter;
    private final GitGroupService gitGroupService;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public CourseEditionService(CourseEditionRepository courseEditionRepository, CourseEditionConverter courseEditionConverter,
                                LessonRepository lessonRepository, CourseEditionLessonRepository courseEditionLessonRepository,
                                GitGroupService gitGroupService, UserDetailsServiceImpl userDetailsService,
                                CourseEditionLessonConverter courseEditionLessonConverter
    ) {
        this.courseEditionRepository = courseEditionRepository;
        this.courseEditionConverter = courseEditionConverter;
        this.lessonRepository = lessonRepository;
        this.courseEditionLessonRepository = courseEditionLessonRepository;
        this.courseEditionLessonConverter = courseEditionLessonConverter;
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

    public CourseEdition createCourseEdition(CourseEditionDto courseEditionDto) throws ExecutionException, InterruptedException {
        CourseEdition courseEdition = courseEditionRepository.save(courseEditionConverter.convertFromDto(courseEditionDto));
        Long gitGroupId = gitGroupService.createGroup(createGroupName(courseEditionDto, courseEdition.getId())).get().getId();
        courseEdition.setGitGroupId(gitGroupId);
        courseEditionRepository.save(courseEdition);
        addTeachersToGroup(gitGroupId);
        return courseEdition;
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
        List<CourseEditionLesson> courseEditionLessons = new ArrayList<>();
        Timestamp startDate = courseEdition.getStartDate();

        for (Lesson lesson : lessons) {
            CourseEditionLesson courseEditionLesson = createCourseEditionLesson(courseEdition, lesson, startDate);
            startDate = addDaysToDate(courseEditionLesson.getEndDate(), 1);
            courseEditionLessons.add(courseEditionLessonRepository.save(courseEditionLesson));
        }
        return courseEditionLessons;
    }

    public Collection<CourseEditionLessonDto> getCourseEditionLessonList(Long editionId) {
        return courseEditionLessonConverter.convertFromEntities(
                courseEditionLessonRepository.findByCourseEdition_Id(editionId)
        );
    }

    public CourseEditionLessonDto getCourseEditionLesson(Long lessonId, Long editionId) {
        return courseEditionLessonConverter.convertFromEntity(
                courseEditionLessonRepository.findByLesson_IdAndCourseEdition_Id(lessonId, editionId)
                        .orElseThrow(() -> new CourseEditionLessonNotFoundException(lessonId, editionId))
        );
    }

    private CourseEditionLesson createCourseEditionLesson(CourseEdition courseEdition, Lesson lesson, Timestamp startDate) {
        CourseEditionLesson courseEditionLesson = new CourseEditionLesson();
        courseEditionLesson.setCourseEdition(courseEdition);
        courseEditionLesson.setLesson(lesson);
        Timestamp endDate = addDaysToDate(startDate, lesson.getTimeInDays());
        courseEditionLesson.setStartDate(startDate);
        courseEditionLesson.setEndDate(endDate);
        return courseEditionLesson;
    }

    private Timestamp addDaysToDate(Timestamp date, int days) {
        return Timestamp.valueOf(date.toLocalDateTime().plusDays(days));
    }

    public void updateCourseEditionLesson(Long id, CourseEditionLessonDto courseEditionLessonDto) {
        courseEditionLessonDto.setId(id);
        CourseEditionLesson courseEditionLesson = courseEditionLessonConverter.convertFromDto(courseEditionLessonDto);
        courseEditionLessonRepository.save(courseEditionLesson);
    }
}
