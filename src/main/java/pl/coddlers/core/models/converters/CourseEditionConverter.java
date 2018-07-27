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
        courseEditionDto.setCourseVersion(entity.getCourseVersion());
        courseEditionDto.setStartDate(entity.getStartDate());
        courseEditionDto.setCourse(entity.getCourse());

        return courseEditionDto;
    }

    @Override
    public CourseEdition convertFromDto(CourseEditionDto dto) {
        CourseEdition courseEdition = new CourseEdition();
        courseEdition.setTitle(dto.getTitle());
        courseEdition.setStartDate(dto.getStartDate());
        courseEdition.setCourseVersion(dto.getCourseVersion());
        courseEdition.setCourse(dto.getCourse());

        return courseEdition;
    }
}
