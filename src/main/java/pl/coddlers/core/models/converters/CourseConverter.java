package pl.coddlers.core.models.converters;

import org.springframework.stereotype.Component;
import pl.coddlers.core.models.dto.CourseDto;
import pl.coddlers.core.models.entity.Course;

@Component
public class CourseConverter implements BaseConverter<Course, CourseDto> {
    @Override
    public CourseDto convertFromEntity(Course entity) {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(entity.getId());
        courseDto.setTitle(entity.getTitle());
        courseDto.setDescription(entity.getDescription());

        return courseDto;
    }

    @Override
    public Course convertFromDto(CourseDto dto) {
        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());

        return course;
    }
}
