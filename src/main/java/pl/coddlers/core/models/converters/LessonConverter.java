package pl.coddlers.core.models.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coddlers.core.exceptions.CourseNotFoundException;
import pl.coddlers.core.models.dto.LessonDTO;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.models.entity.Course;
import pl.coddlers.core.repositories.LessonRepository;
import pl.coddlers.core.repositories.CourseRepository;

@Component
public class LessonConverter implements BaseConverter<Lesson, LessonDTO> {

	private final LessonRepository lessonRepository;

	private final CourseRepository courseRepository;

	@Autowired
	public LessonConverter(LessonRepository lessonRepository, CourseRepository courseRepository) {
		this.lessonRepository = lessonRepository;
		this.courseRepository = courseRepository;
	}

	@Override
	public LessonDTO convertFromEntity(Lesson entity) {
		LessonDTO lessonDTO = new LessonDTO();
		lessonDTO.setId(entity.getId());
		lessonDTO.setCourseId(entity.getCourse().getId());
		lessonDTO.setDescription(entity.getDescription());
		lessonDTO.setWeight(entity.getWeight());
		lessonDTO.setDueDate(entity.getDueDate());
		lessonDTO.setStartDate(entity.getStartDate());
		lessonDTO.setTitle(entity.getTitle());

		return lessonDTO;
	}

	@Override
	public Lesson convertFromDto(LessonDTO dto) {
		Lesson lesson = new Lesson();

		if (dto.getId() != null && lessonRepository.existsById(dto.getId())) {
			lesson.setId(dto.getId());
		}

		Course course = courseRepository.getById(dto.getCourseId())
				.orElseThrow(() -> new CourseNotFoundException(dto.getCourseId()));

		lesson.setCourse(course);
		lesson.setDescription(dto.getDescription());
		lesson.setWeight(dto.getWeight());
		lesson.setDueDate(dto.getDueDate());
		lesson.setStartDate(dto.getStartDate());
		lesson.setTitle(dto.getTitle());

		// TODO only for prototype purposes
		lesson.setGitStudentProjectId(dto.getGitStudentProjectId());

		return lesson;
	}
}
