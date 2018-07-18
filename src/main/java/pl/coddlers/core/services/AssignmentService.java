package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.AssignmentNotFoundException;
import pl.coddlers.core.models.converters.AssignmentConverter;
import pl.coddlers.core.models.dto.AssignmentDto;
import pl.coddlers.core.models.entity.Assignment;
import pl.coddlers.core.repositories.AssignmentRepository;

import java.util.Collection;

@Service
public class AssignmentService {

	@Autowired
	private AssignmentRepository assignmentRepository;
	@Autowired
	private AssignmentConverter assignmentConverter;

	public Collection<AssignmentDto> getAllCoursesAssignments(long courseId) {
		return assignmentConverter.convertFromEntities(assignmentRepository.findByCourse_Id(courseId));
	}

	public Long createAssignment(AssignmentDto assignmentDto) {
		Assignment assignment = assignmentConverter.convertFromDto(assignmentDto);

		assignmentRepository.save(assignment);

		return assignment.getId();
	}

	public AssignmentDto getAssignmentById(Long id) {
		Assignment assignment = validateAssignment(id);

		return assignmentConverter.convertFromEntity(assignment);
	}

	public AssignmentDto updateAssigment(Long id, AssignmentDto assignmentDto) {
		validateAssignment(id);

		assignmentDto.setId(id);
		Assignment assignment = assignmentConverter.convertFromDto(assignmentDto);
		assignmentRepository.save(assignment);

		return assignmentDto;
	}

	private Assignment validateAssignment(Long id) throws AssignmentNotFoundException {
		return assignmentRepository.findById(id).orElseThrow(
				() -> new AssignmentNotFoundException(id)
		);
	}
}
