package pl.coddlers.services;

import org.springframework.beans.factory.annotation.Autowired;
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
        validateCourse(id);

        return courseConverter.convertFromEntity(courseRepository.getById(id).get());
    }

    public Collection<CourseDto> getCourses(Long startsAt, Long number) throws WrongDateException {
        List<Course> courseList;

        if (startsAt == null && number == null) {
            courseList = courseRepository.findAll();
        } else if (number == null) {
            if (startsAt < 1 ) {
                throw new WrongParametersException("startsAt <= 1 ");
            }

            courseList = courseRepository.getCoursesWithIdGreaterEqThan(startsAt);
        } else if (startsAt == null) {
            if (number < 0) {
                throw new WrongParametersException("number < 0");
            }

            courseList = courseRepository.getCoursesWithIdFromInclusiveRange(1l, number);
        } else {
            if (startsAt < 1 || number < 0) {
                throw new WrongParametersException("startsAt <= 1 || number < 0");
            }

            courseList = courseRepository.getCoursesWithIdFromInclusiveRange(startsAt, startsAt+number-1);
        }

        return courseConverter.convertFromEntities(courseList);
    }

    public Course createCourse(final CourseDto courseDto) {
        this.validateCourse(courseDto);

        return courseRepository.save(courseConverter.convertFromDto(courseDto));
    }

    private void validateCourse(Long id) throws CourseNotFoundException {
        courseRepository.findById(id).orElseThrow(
                () -> new CourseNotFoundException(id)
        );
    }

    private void validateCourse(CourseDto courseDto) throws WrongDateException {
        if(courseDto.getStartDate().after(courseDto.getEndDate())) {
            throw new WrongDateException();
        }
    }
}
