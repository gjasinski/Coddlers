package pl.coddlers.core.models.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coddlers.core.models.dto.CourseDto;
import pl.coddlers.core.models.entity.Course;
import pl.coddlers.core.repositories.CourseRepository;

@Component
public class CourseConverter implements BaseConverter<Course, CourseDto> {
    private CourseRepository courseRepository;

    @Autowired
    public CourseConverter(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

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
        if (dto.getId() != null && courseRepository.existsById(dto.getId())) {
            course = courseRepository.findById(dto.getId()).get();
        }
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());

        return course;
    }
}
