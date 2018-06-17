package pl.coddlers.models.converters;

import org.springframework.stereotype.Component;
import pl.coddlers.models.dto.TaskDto;
import pl.coddlers.models.entity.Task;
import pl.coddlers.models.entity.TaskStatus;

@Component
public class TaskConverter implements BaseConverter<Task, TaskDto> { ;

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
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setMaxPoints(dto.getMaxPoints());
        if (dto.getTaskStatus() == null)
            task.setTaskStatus(TaskStatus.NOT_SUBMITTED);
        else
            task.setTaskStatus(dto.getTaskStatus());
        return task;
    }
}
