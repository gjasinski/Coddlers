package pl.coddlers.models.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coddlers.models.dto.AssignmentDto;
import pl.coddlers.models.entity.Assignment;
import pl.coddlers.models.entity.Course;
import pl.coddlers.repositories.CourseRepository;

@Component
public class AssignmentConverter implements BaseConverter<Assignment, AssignmentDto> {
	@Autowired
	CourseRepository courseRepository;

	@Override
	public AssignmentDto convertFromEntity(Assignment entity) {
		AssignmentDto assignmentDto = new AssignmentDto();
		assignmentDto.setId(entity.getId());
		assignmentDto.setCourseId(entity.getCourse().getId());
		assignmentDto.setDescription(entity.getDescription());
		assignmentDto.setDueDate(entity.getDueDate());
		assignmentDto.setStartDate(entity.getStartDate());
		assignmentDto.setTitle(entity.getTitle());
		return assignmentDto;
	}

	@Override
	public Assignment convertFromDto(AssignmentDto dto) {
		Course course = courseRepository.getById(dto.getCourseId())
				.orElseThrow(() -> new IllegalArgumentException("Course does not exist"));
		Assignment assignment = new Assignment();
		assignment.setDescription(dto.getDescription());
		assignment.setDueDate(dto.getDueDate());
		assignment.setStartDate(dto.getStartDate());
		assignment.setTitle(dto.getTitle());
		assignment.setCourse(course);
		return assignment;
	}
}
