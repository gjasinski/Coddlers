package pl.coddlers.core.models.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coddlers.core.models.dto.LessonDto;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.repositories.CourseRepository;
import pl.coddlers.core.repositories.LessonRepository;

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
		lessonDto.setTitle(entity.getTitle());
		lessonDto.setDescription(entity.getDescription());
		lessonDto.setWeight(entity.getWeight());
		lessonDto.setTimeInDays(entity.getTimeInDays());

		return lessonDto;
	}

	@Override
	public Lesson convertFromDto(LessonDto dto) {
		Lesson lesson = new Lesson();

		if (dto.getId() != null && lessonRepository.existsById(dto.getId())) {
			lesson.setId(dto.getId());
		}

		lesson.setTitle(dto.getTitle());
		lesson.setDescription(dto.getDescription());
		lesson.setWeight(dto.getWeight());
		lesson.setTimeInDays(dto.getTimeInDays());

		// TODO only for prototype purposes
		lesson.setGitStudentProjectId(dto.getGitStudentProjectId());

		return lesson;
	}
}
