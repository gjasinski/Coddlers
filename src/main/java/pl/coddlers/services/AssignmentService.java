package pl.coddlers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.models.converters.AssignmentConverter;
import pl.coddlers.models.dto.AssignmentDto;
import pl.coddlers.models.entity.Assignment;
import pl.coddlers.models.entity.Course;
import pl.coddlers.repositories.AssignmentRepository;
import pl.coddlers.repositories.CourseRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

	@Autowired
	private AssignmentRepository assignmentRepository;
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private AssignmentConverter assignmentConverter;

	public Collection<AssignmentDto> getAllCoursesAssignments(long courseId) {
		Optional<Course> course = courseRepository.getById(courseId);
		if (course.isPresent()) {
			return assignmentConverter
					.convertFromEntities(course.get().getAssignmentList());
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
