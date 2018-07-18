package pl.coddlers.core.models.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coddlers.core.models.dto.AssignmentDto;
import pl.coddlers.core.models.entity.Assignment;
import pl.coddlers.core.models.entity.Course;
import pl.coddlers.core.repositories.AssignmentRepository;
import pl.coddlers.core.repositories.CourseRepository;

@Component
public class AssignmentConverter implements BaseConverter<Assignment, AssignmentDto> {

	@Autowired
	AssignmentRepository assignmentRepository;

	@Autowired
	CourseRepository courseRepository;

	@Override
	public AssignmentDto convertFromEntity(Assignment entity) {
		AssignmentDto assignmentDto = new AssignmentDto();
		assignmentDto.setId(entity.getId());
		assignmentDto.setCourseId(entity.getCourse().getId());
		assignmentDto.setDescription(entity.getDescription());
		assignmentDto.setWeight(entity.getWeight());
		assignmentDto.setDueDate(entity.getDueDate());
		assignmentDto.setStartDate(entity.getStartDate());
		assignmentDto.setTitle(entity.getTitle());

		return assignmentDto;
	}

	@Override
	public Assignment convertFromDto(AssignmentDto dto) {
		Assignment assignment = new Assignment();

		if (dto.getId() != null && assignmentRepository.existsById(dto.getId())) {
			assignment.setId(dto.getId());
		}

		Course course = courseRepository.getById(dto.getCourseId())
				.orElseThrow(() -> new IllegalArgumentException("Course does not exist"));

		assignment.setCourse(course);
		assignment.setDescription(dto.getDescription());
		assignment.setWeight(dto.getWeight());
		assignment.setDueDate(dto.getDueDate());
		assignment.setStartDate(dto.getStartDate());
		assignment.setTitle(dto.getTitle());

		// TODO only for prototype purposes
		assignment.setGitStudentProjectId(dto.getGitStudentProjectId());

		return assignment;
	}
}
