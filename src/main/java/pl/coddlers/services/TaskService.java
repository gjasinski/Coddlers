package pl.coddlers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.exceptions.TaskNotFoundException;
import pl.coddlers.models.converters.TaskConverter;
import pl.coddlers.models.dto.TaskDto;
import pl.coddlers.models.entity.Assignment;
import pl.coddlers.models.entity.Task;
import pl.coddlers.models.entity.TaskStatus;
import pl.coddlers.repositories.AssignmentRepository;
import pl.coddlers.repositories.TaskRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskConverter taskConverter;

    @Autowired
    private AssignmentRepository assignmentRepository;

    public Collection<TaskDto> getAllAssignmentsTasks(long assignmentId) {
        Optional<Assignment> assignment = assignmentRepository.findById(assignmentId);
        if (assignment.isPresent()) {
            return taskConverter
                    .convertFromEntities(assignment.get().getTaskList());
        }
        return Collections.emptyList();
    }

    public Long createTask(final TaskDto taskDto) {
        Task task = taskConverter.convertFromDto(taskDto);
        task.setTaskStatus(TaskStatus.NOT_SUBMITTED);
        taskRepository.save(task);
        return task.getId();
    }

    public void editTask(final TaskDto taskDto) {
        Task task = taskConverter.convertFromDto(taskDto);
        task.setId(taskDto.getId());
        if (task.getTaskStatus() == null)
            task.setTaskStatus(TaskStatus.NOT_SUBMITTED);
        taskRepository.save(task);
    }

    public TaskDto getTaskById(Long id) {
        Task task = validateTask(id);

        return taskConverter.convertFromEntity(task);
    }

    private Task validateTask(Long id) throws TaskNotFoundException {
        return taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException(id)
        );
    }
}
