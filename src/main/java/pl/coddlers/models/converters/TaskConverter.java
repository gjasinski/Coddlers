package pl.coddlers.models.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coddlers.models.dto.TaskDto;
import pl.coddlers.models.entity.Assignment;
import pl.coddlers.models.entity.Task;
import pl.coddlers.models.entity.TaskStatus;
import pl.coddlers.repositories.AssignmentRepository;

@Component
public class TaskConverter implements BaseConverter<Task, TaskDto> {
    @Autowired
    AssignmentRepository assignmentRepository;

    @Override
    public TaskDto convertFromEntity(Task entity) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(entity.getId());
        taskDto.setAssignmentId(entity.getAssignment().getId());
        taskDto.setTitle(entity.getTitle());
        taskDto.setDescription(entity.getDescription());
        taskDto.setWeight(entity.getWeight());
        taskDto.setMaxPoints(entity.getMaxPoints());
        taskDto.setTaskStatus(entity.getTaskStatus());
        return taskDto;
    }

    @Override
    public Task convertFromDto(TaskDto dto) {
        Assignment assignment = assignmentRepository.findById(dto.getAssignmentId())
                .orElseThrow(() -> new IllegalArgumentException("Assignment does not exist"));
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setAssignment(assignment);
        task.setDescription(dto.getDescription());
        task.setWeight(dto.getWeight());
        task.setMaxPoints(dto.getMaxPoints());
        if (dto.getTaskStatus() == null)
            task.setTaskStatus(TaskStatus.NOT_SUBMITTED);
        else
            task.setTaskStatus(dto.getTaskStatus());
        return task;
    }
}
