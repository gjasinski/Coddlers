package pl.coddlers.models.converters;

import org.springframework.stereotype.Component;
import pl.coddlers.models.dto.CourseDto;
import pl.coddlers.models.entity.Course;

@Component
public class CourseConverter implements BaseConverter<Course, CourseDto> {
    @Override
    public CourseDto convertFromEntity(Course entity) {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(entity.getId());
        courseDto.setTitle(entity.getTitle());
        courseDto.setDescription(entity.getDescription());
        courseDto.setStartDate(entity.getStartDate());
        courseDto.setEndDate(entity.getEndDate());

        return courseDto;
    }

    @Override
    public Course convertFromDto(CourseDto dto) {
        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setStartDate(dto.getStartDate());
        course.setEndDate(dto.getEndDate());
        course.setDescription(dto.getDescription());

        return course;
    }
}
