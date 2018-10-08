package pl.coddlers.core.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.CouldNotCreateGitTaskException;
import pl.coddlers.core.exceptions.InternalServerErrorException;
import pl.coddlers.core.exceptions.TaskNotFoundException;
import pl.coddlers.core.models.converters.TaskConverter;
import pl.coddlers.core.models.dto.TaskDto;
import pl.coddlers.core.models.entity.Task;
import pl.coddlers.core.repositories.TaskRepository;
import pl.coddlers.git.services.GitTaskService;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class TaskService {

	private final TaskRepository taskRepository;

	private final TaskConverter taskConverter;

	private final GitTaskService gitTaskService;

	@Autowired
	public TaskService(TaskRepository taskRepository, TaskConverter taskConverter, GitTaskService gitTaskService) {
		this.taskRepository = taskRepository;
		this.taskConverter = taskConverter;
		this.gitTaskService = gitTaskService;
	}

	public Collection<TaskDto> getAllLessonsTasks(long lessonId) {
		return taskConverter.convertFromEntities(taskRepository.findByLessonId(lessonId));
	}

	public Task createTask(final TaskDto taskDto) {
		Task task = taskConverter.convertFromDto(taskDto);
		String taskBranchName = createBranchNamePrefix(task);
		task.setBranchNamePrefix(taskBranchName);

		try {
			Boolean isCreated = gitTaskService.createTask(task.getLesson().getGitProjectId(), taskBranchName)
					.exceptionally(ex -> {
						log.error(ex.getMessage());
						return false;
					})
					.get();
			if (!isCreated) {
				throw new CouldNotCreateGitTaskException(taskBranchName);
			}

			taskRepository.save(task);
			return task;
		} catch (InterruptedException | ExecutionException | CouldNotCreateGitTaskException e) {
			log.error(e.getMessage());
			throw new InternalServerErrorException("There was some error while creating a task. Please contact with administrator.");
		}
	}

	private String getCurrentTimestamp() {
		Date date= new Date();
		long time = date.getTime();
		return Long.toString(time);
	}

	private String createBranchNamePrefix(Task task) {
		return String.format("%s_%s", task.getTitle().toLowerCase().replaceAll("\\s+","-"),
				getCurrentTimestamp());
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
