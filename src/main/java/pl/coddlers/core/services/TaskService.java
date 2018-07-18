package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.TaskNotFoundException;
import pl.coddlers.core.models.converters.TaskConverter;
import pl.coddlers.core.models.dto.TaskDto;
import pl.coddlers.core.models.entity.Task;
import pl.coddlers.core.repositories.AssignmentRepository;
import pl.coddlers.core.repositories.TaskRepository;

import java.util.Collection;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final TaskConverter taskConverter;

    private final AssignmentRepository assignmentRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskConverter taskConverter, AssignmentRepository assignmentRepository) {
        this.taskRepository = taskRepository;
        this.taskConverter = taskConverter;
        this.assignmentRepository = assignmentRepository;
    }

    public Collection<TaskDto> getAllAssignmentsTasks(long assignmentId) {
        return taskConverter.convertFromEntities(taskRepository.findByAssignment_Id(assignmentId));
    }

    public Task createTask(final TaskDto taskDto) {
        Task task = taskConverter.convertFromDto(taskDto);
        taskRepository.save(task);

        return task;
    }

    public void updateTask(Long id, final TaskDto taskDto) {
        validateTask(id);

        taskDto.setId(id);
        Task task = taskConverter.convertFromDto(taskDto);

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
