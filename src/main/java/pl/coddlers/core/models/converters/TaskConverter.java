package pl.coddlers.core.models.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coddlers.core.models.dto.TaskDto;
import pl.coddlers.core.models.entity.Assignment;
import pl.coddlers.core.models.entity.Task;
import pl.coddlers.core.models.entity.TaskStatus;
import pl.coddlers.core.repositories.AssignmentRepository;
import pl.coddlers.core.repositories.TaskRepository;

@Component
public class TaskConverter implements BaseConverter<Task, TaskDto> { ;

    private final AssignmentRepository assignmentRepository;

    private final TaskRepository taskRepository;

    @Autowired
    public TaskConverter(AssignmentRepository assignmentRepository, TaskRepository taskRepository) {
        this.assignmentRepository = assignmentRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskDto convertFromEntity(Task entity) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(entity.getId());
        taskDto.setAssignmentId(entity.getAssignment().getId());
        taskDto.setTitle(entity.getTitle());
        taskDto.setDescription(entity.getDescription());
        taskDto.setMaxPoints(entity.getMaxPoints());
        taskDto.setTaskStatus(entity.getTaskStatus());

        return taskDto;
    }

    @Override
    public Task convertFromDto(TaskDto dto) {
        Task task = new Task();

        if (dto.getId() != null && taskRepository.existsById(dto.getId())) {
            task.setId(dto.getId());
        }

        Assignment assignment = assignmentRepository.findById(dto.getAssignmentId())
                .orElseThrow(() -> new IllegalArgumentException("Assignment does not exist"));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setMaxPoints(dto.getMaxPoints());
        task.setAssignment(assignment);

        if (dto.getTaskStatus() == null) {
            task.setTaskStatus(TaskStatus.NOT_SUBMITTED);
        } else {
            task.setTaskStatus(dto.getTaskStatus());
        }

        return task;
    }
}