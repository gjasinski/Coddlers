package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.CourseNotFoundException;
import pl.coddlers.core.exceptions.WrongDateException;
import pl.coddlers.core.models.converters.CourseConverter;
import pl.coddlers.core.models.dto.CourseDto;
import pl.coddlers.core.models.entity.Course;
import pl.coddlers.core.models.entity.CourseVersion;
import pl.coddlers.core.models.entity.Teacher;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.core.repositories.CourseRepository;
import pl.coddlers.core.repositories.CourseVersionRepository;
import pl.coddlers.core.repositories.TeacherRepository;

import java.util.Collection;
import java.util.List;

@Service
public class CourseService {

	private final CourseRepository courseRepository;
	private final CourseConverter courseConverter;
	private final UserDetailsServiceImpl userDetailsService;
	private final CourseVersionRepository courseVersionRepository;
	private final TeacherRepository teacherRepository;

	@Autowired
	public CourseService(CourseRepository courseRepository, CourseConverter courseConverter,
						 UserDetailsServiceImpl userDetailsService, CourseVersionRepository courseVersionRepository,
						 TeacherRepository teacherRepository) {
		this.courseRepository = courseRepository;
		this.courseConverter = courseConverter;
		this.userDetailsService = userDetailsService;
		this.courseVersionRepository = courseVersionRepository;
		this.teacherRepository = teacherRepository;
	}

	public CourseDto getCourseById(Long id) {
		Course course = validateCourse(id);

		return courseConverter.convertFromEntity(course);
	}

	public Collection<CourseDto> getCourses() throws WrongDateException {
		List<Course> courseList = courseRepository.getByUserId(userDetailsService.getCurrentUserEntity().getId());
		return courseConverter.convertFromEntities(courseList);
	}

	public Course createCourse(final CourseDto courseDto) {
		Course course = courseRepository.save(courseConverter.convertFromDto(courseDto));
		User currentUser = userDetailsService.getCurrentUserEntity();
		CourseVersion courseVersion = new CourseVersion(1, course);
		Teacher teacher = new Teacher(course, currentUser, true);
		courseVersionRepository.save(courseVersion);
		teacherRepository.save(teacher);

		return course;
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

}
