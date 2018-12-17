package pl.coddlers.core.models.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coddlers.core.exceptions.LessonNotFoundException;
import pl.coddlers.core.models.dto.TaskDto;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.models.entity.Task;
import pl.coddlers.core.repositories.LessonRepository;
import pl.coddlers.core.repositories.TaskRepository;

@Component
public class TaskConverter implements BaseConverter<Task, TaskDto> {

    private final LessonRepository lessonRepository;

    private final TaskRepository taskRepository;

    @Autowired
    public TaskConverter(LessonRepository lessonRepository, TaskRepository taskRepository) {
        this.lessonRepository = lessonRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskDto convertFromEntity(Task entity) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(entity.getId());
        taskDto.setLessonId(entity.getLesson().getId());
        taskDto.setTitle(entity.getTitle());
        taskDto.setDescription(entity.getDescription());
        taskDto.setMaxPoints(entity.getMaxPoints());
        taskDto.setIsCodeTask(entity.getIsCodeTask());
        taskDto.setBranchNamePrefix(entity.getBranchNamePrefix());

        return taskDto;
    }

    @Override
    public Task convertFromDto(TaskDto dto) {
        Task task = new Task();
        if (dto.getId() != null && taskRepository.existsById(dto.getId())) {
            task = taskRepository.findById(dto.getId()).get();
        }

        Lesson lesson = lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new LessonNotFoundException(dto.getLessonId()));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setMaxPoints(dto.getMaxPoints());
        task.setLesson(lesson);
        task.setIsCodeTask(dto.getIsCodeTask());
        task.setBranchNamePrefix(dto.getBranchNamePrefix());

        return task;
    }
}
