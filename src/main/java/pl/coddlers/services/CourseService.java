package pl.coddlers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.coddlers.exceptions.CourseNotFoundException;
import pl.coddlers.exceptions.WrongDateException;
import pl.coddlers.exceptions.WrongParametersException;
import pl.coddlers.models.converters.CourseConverter;
import pl.coddlers.models.dto.CourseDto;
import pl.coddlers.models.entity.Course;
import pl.coddlers.repositories.CourseRepository;
import java.util.Collection;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseConverter courseConverter;

    public CourseDto getCourseById(Long id) {
        Course course = validateCourse(id);

        return courseConverter.convertFromEntity(course);
    }

    public Collection<CourseDto> getCourses(Integer startsAt, Integer number) throws WrongDateException {
        List<Course> courseList;

        if (startsAt == null && number == null) {
            courseList = courseRepository.findAll();
        } else if (number == null) {
            if (startsAt < 1 ) {
                throw new WrongParametersException("startsAt <= 1 ");
            }

            courseList = courseRepository.getPaginatedCourses(PageRequest.of((startsAt-1), Integer.MAX_VALUE));
        } else if (startsAt == null) {
            if (number < 0) {
                throw new WrongParametersException("number < 0");
            }

            courseList = courseRepository.getPaginatedCourses(PageRequest.of(0, number));
        } else {
            if (startsAt < 1 || number < 0) {
                throw new WrongParametersException("startsAt <= 1 || number < 0");
            }

            courseList = courseRepository.getPaginatedCourses(PageRequest.of(startsAt-1, number));
        }

        return courseConverter.convertFromEntities(courseList);
    }

    public Course createCourse(final CourseDto courseDto) {
        this.validateCourse(courseDto);

        return courseRepository.save(courseConverter.convertFromDto(courseDto));
    }

    private Course validateCourse(Long id) throws CourseNotFoundException {
        return courseRepository.findById(id).orElseThrow(
                () -> new CourseNotFoundException(id)
        );
    }

    private void validateCourse(CourseDto courseDto) throws WrongDateException {
        if(courseDto.getStartDate().after(courseDto.getEndDate())) {
            throw new WrongDateException();
        }
    }
}
