package pl.coddlers.core.models.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coddlers.core.exceptions.CourseEditionNotFoundException;
import pl.coddlers.core.exceptions.LessonNotFoundException;
import pl.coddlers.core.models.dto.CourseEditionLessonDto;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.CourseEditionLesson;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.repositories.CourseEditionLessonRepository;
import pl.coddlers.core.repositories.CourseEditionRepository;
import pl.coddlers.core.repositories.LessonRepository;

@Component
public class CourseEditionLessonConverter implements BaseConverter<CourseEditionLesson, CourseEditionLessonDto> {

    private CourseEditionLessonRepository courseEditionLessonRepository;
    private LessonRepository lessonRepository;
    private CourseEditionRepository courseEditionRepository;

    @Autowired
    public CourseEditionLessonConverter(CourseEditionLessonRepository courseEditionLessonRepository,
                                        LessonRepository lessonRepository,
                                        CourseEditionRepository courseEditionRepository) {
        this.courseEditionLessonRepository = courseEditionLessonRepository;
        this.lessonRepository = lessonRepository;
        this.courseEditionRepository = courseEditionRepository;
    }

    @Override
    public CourseEditionLessonDto convertFromEntity(CourseEditionLesson entity) {
        CourseEditionLessonDto courseEditionLessonDto = new CourseEditionLessonDto();
        courseEditionLessonDto.setId(entity.getId());
        courseEditionLessonDto.setStartDate(entity.getStartDate());
        courseEditionLessonDto.setEndDate(entity.getEndDate());
        courseEditionLessonDto.setLessonId(entity.getLesson().getId());
        courseEditionLessonDto.setCourseEditionId(entity.getCourseEdition().getId());

        return courseEditionLessonDto;
    }

    @Override
    public CourseEditionLesson convertFromDto(CourseEditionLessonDto dto) {
        CourseEditionLesson courseEditionLesson = new CourseEditionLesson();

        if (dto.getId() != null && courseEditionLessonRepository.existsById(dto.getId())) {
            courseEditionLesson.setId(dto.getId());
        }
        Lesson lesson = lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new LessonNotFoundException(dto.getLessonId()));
        CourseEdition courseEdition = courseEditionRepository.findById(dto.getCourseEditionId())
                .orElseThrow(() -> new CourseEditionNotFoundException(dto.getCourseEditionId()));
        courseEditionLesson.setStartDate(dto.getStartDate());
        courseEditionLesson.setEndDate(dto.getEndDate());
        courseEditionLesson.setLesson(lesson);
        courseEditionLesson.setCourseEdition(courseEdition);

        return courseEditionLesson;
    }
}
