package pl.coddlers.models.converters;

import org.springframework.stereotype.Component;
import pl.coddlers.models.dto.AssignmentDto;
import pl.coddlers.models.entity.Assignment;

@Component
public class AssignmentConverter implements BaseConverter<Assignment, AssignmentDto> {

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
		assignment.setDescription(dto.getDescription());
		assignment.setWeight(dto.getWeight());
		assignment.setDueDate(dto.getDueDate());
		assignment.setStartDate(dto.getStartDate());
		assignment.setTitle(dto.getTitle());
		return assignment;
	}
}
