package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.LessonNotFoundException;
import pl.coddlers.core.models.converters.LessonConverter;
import pl.coddlers.core.models.dto.LessonDTO;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.repositories.LessonRepository;

import java.util.Collection;

@Service
public class LessonService {

	private final LessonRepository lessonRepository;
	private final LessonConverter lessonConverter;

	@Autowired
	public LessonService(LessonRepository lessonRepository, LessonConverter lessonConverter) {
		this.lessonRepository = lessonRepository;
		this.lessonConverter = lessonConverter;
	}

	public Collection<LessonDTO> getAllCoursesLessons(long courseId) {
		return lessonConverter.convertFromEntities(lessonRepository.findByCourse_Id(courseId));
	}

	public Long createLesson(LessonDTO lessonDTO) {
		Lesson lesson = lessonConverter.convertFromDto(lessonDTO);

		lessonRepository.save(lesson);

		return lesson.getId();
	}

	public LessonDTO getLessonById(Long id) {
		Lesson lesson = validateLesson(id);

		return lessonConverter.convertFromEntity(lesson);
	}

	public LessonDTO updateLesson(Long id, LessonDTO lessonDTO) {
		validateLesson(id);

		lessonDTO.setId(id);
		Lesson lesson = lessonConverter.convertFromDto(lessonDTO);
		lessonRepository.save(lesson);

		return lessonDTO;
	}

	private Lesson validateLesson(Long id) throws LessonNotFoundException {
		return lessonRepository.findById(id)
				.orElseThrow(() -> new LessonNotFoundException(id));
	}
}
