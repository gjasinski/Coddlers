package pl.coddlers.core.models.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coddlers.core.exceptions.CourseNotFoundException;
import pl.coddlers.core.models.dto.LessonDto;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.models.entity.Course;
import pl.coddlers.core.repositories.LessonRepository;
import pl.coddlers.core.repositories.CourseRepository;

@Component
public class LessonConverter implements BaseConverter<Lesson, LessonDto> {

	private final LessonRepository lessonRepository;

	private final CourseRepository courseRepository;

	@Autowired
	public LessonConverter(LessonRepository lessonRepository, CourseRepository courseRepository) {
		this.lessonRepository = lessonRepository;
		this.courseRepository = courseRepository;
	}

	@Override
	public LessonDto convertFromEntity(Lesson entity) {
		LessonDto lessonDto = new LessonDto();
		lessonDto.setId(entity.getId());
		lessonDto.setCourseId(entity.getCourse().getId());
		lessonDto.setDescription(entity.getDescription());
		lessonDto.setWeight(entity.getWeight());
		lessonDto.setDueDate(entity.getDueDate());
		lessonDto.setStartDate(entity.getStartDate());
		lessonDto.setTitle(entity.getTitle());

		return lessonDto;
	}

	@Override
	public Lesson convertFromDto(LessonDto dto) {
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
