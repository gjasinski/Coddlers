package pl.coddlers.core.models.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coddlers.core.models.dto.CourseEditionDto;
import pl.coddlers.core.models.entity.Course;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.repositories.CourseRepository;

@Component
public class CourseEditionConverter implements BaseConverter<CourseEdition, CourseEditionDto> {

    @Autowired
    CourseRepository courseRepository;

    @Override
    public CourseEditionDto convertFromEntity(CourseEdition entity) {
        CourseEditionDto courseEditionDto = new CourseEditionDto();
        courseEditionDto.setId(entity.getId());
        courseEditionDto.setTitle(entity.getTitle());
        courseEditionDto.setVersion(entity.getVersion());
        courseEditionDto.setStartDate(entity.getStartDate());
        courseEditionDto.setCourseId(entity.getCourseId());

        Course course = courseRepository.getById(courseEditionDto.getCourseId()).get();
        courseEditionDto.setEndDate(course.getEndDate());

        return courseEditionDto;
    }

    @Override
    public CourseEdition convertFromDto(CourseEditionDto dto) {
        CourseEdition courseEdition = new CourseEdition();
        courseEdition.setTitle(dto.getTitle());
        courseEdition.setStartDate(dto.getStartDate());
        courseEdition.setVersion(dto.getVersion());
        courseEdition.setCourseId(dto.getCourseId());

        return courseEdition;
    }
}
