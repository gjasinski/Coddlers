package pl.coddlers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.models.converters.AssignmentConverter;
import pl.coddlers.models.dto.AssignmentDto;
import pl.coddlers.models.entity.Assignment;
import pl.coddlers.models.entity.Course;
import pl.coddlers.repositories.AssignmentRepository;
import pl.coddlers.repositories.CourseRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssignmentService {
	private final AssignmentRepository assignmentRepository;
	private final CourseRepository courseRepository;
	private final AssignmentConverter assignmentConverter = new AssignmentConverter();

	@Autowired
	public AssignmentService(CourseRepository courseRepository, AssignmentRepository assignmentRepository) {
		this.courseRepository = courseRepository;
		this.assignmentRepository = assignmentRepository;
	}

	public List<AssignmentDto> getAllCoursesAssignments(long courseId) {
		Optional<Course> course = courseRepository.getById(courseId);
		if (course.isPresent()) {
			return course.get()
					.getAssignmentList()
					.stream()
					.map(assignmentConverter::convertFromEntity)
					.collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	public Long createAssignment(Long courseId, AssignmentDto assignmentDto) {
		assignmentDto.setCourseId(courseId);
		Assignment assignment = assignmentConverter.convertFromDto(assignmentDto);
		assignmentRepository.save(assignment);
		return assignment.getId();
	}
}
