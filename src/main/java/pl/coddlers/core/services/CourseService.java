package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.CourseNotFoundException;
import pl.coddlers.core.exceptions.WrongDateException;
import pl.coddlers.core.models.converters.CourseConverter;
import pl.coddlers.core.models.dto.CourseDto;
import pl.coddlers.core.models.entity.Course;
import pl.coddlers.core.repositories.CourseRepository;

import java.util.Collection;
import java.util.List;

@Service
public class CourseService {

	private final CourseRepository courseRepository;

	private final CourseConverter courseConverter;

	@Autowired
	public CourseService(CourseRepository courseRepository, CourseConverter courseConverter) {
		this.courseRepository = courseRepository;
		this.courseConverter = courseConverter;
	}

	public CourseDto getCourseById(Long id) {
		Course course = validateCourse(id);

		return courseConverter.convertFromEntity(course);
	}

	public Collection<CourseDto> getCourses(Integer page, Integer size) throws WrongDateException {
		PageableValidation pageableValidation = new PageableValidation(page, size);
		List<Course> courseList = courseRepository.getPaginatedCourses(pageableValidation.createPageRequest());
		return courseConverter.convertFromEntities(courseList);
	}

	public Course createCourse(final CourseDto courseDto) {
		this.validateCourse(courseDto);

		return courseRepository.save(courseConverter.convertFromDto(courseDto));
	}

	public void updateCourse(final CourseDto courseDto) {
		Course course = courseConverter.convertFromDto(courseDto);
		course.setId(courseDto.getId());
		courseRepository.save(course);
	}

	private Course validateCourse(Long id) throws CourseNotFoundException {
		return courseRepository.findById(id).orElseThrow(
				() -> new CourseNotFoundException(id)
		);
	}

	private void validateCourse(CourseDto courseDto) throws WrongDateException {
		if (courseDto.getStartDate().after(courseDto.getEndDate())) {
			throw new WrongDateException();
		}
	}
}
