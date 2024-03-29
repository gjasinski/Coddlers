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

import java.text.Normalizer;
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

	// TODO this logic should be moved to git services
	private String createBranchNamePrefix(Task task) {
		// remove accents
		String taskName = Normalizer.normalize(task.getTitle(), Normalizer.Form.NFD);
		taskName = taskName.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
				.replaceAll("[^\\p{ASCII}]", "");
		// regex match also no-break spaces
		return String.format("%s_%s", taskName.toLowerCase().replaceAll("(\\s+|[\u202F\u00A0])","-"),
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
