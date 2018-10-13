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
import pl.coddlers.core.repositories.CourseEditionLessonRepository;
import pl.coddlers.core.repositories.CourseEditionRepository;
import pl.coddlers.core.repositories.LessonRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseEditionService {
    private final CourseEditionRepository courseEditionRepository;
    private final CourseEditionConverter courseEditionConverter;
    private final LessonRepository lessonRepository;
    private final CourseEditionLessonRepository courseEditionLessonRepository;
    private final CourseEditionLessonConverter courseEditionLessonConverter;

    @Autowired
    public CourseEditionService(CourseEditionRepository courseEditionRepository,
                                CourseEditionConverter courseEditionConverter, LessonRepository lessonRepository,
                                CourseEditionLessonRepository courseEditionLessonRepository,
                                CourseEditionLessonConverter courseEditionLessonConverter) {
        this.courseEditionRepository = courseEditionRepository;
        this.courseEditionConverter = courseEditionConverter;
        this.lessonRepository = lessonRepository;
        this.courseEditionLessonRepository = courseEditionLessonRepository;
        this.courseEditionLessonConverter = courseEditionLessonConverter;
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
        return courseEditionRepository.save(courseEditionConverter.convertFromDto(courseEditionDto));

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
