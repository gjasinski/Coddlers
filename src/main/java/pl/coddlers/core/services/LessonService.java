package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.CourseVersionNotFound;
import pl.coddlers.core.exceptions.LessonNotFoundException;
import pl.coddlers.core.models.converters.LessonConverter;
import pl.coddlers.core.models.dto.LessonDto;
import pl.coddlers.core.models.entity.CourseVersion;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.repositories.CourseVersionRepository;
import pl.coddlers.core.repositories.LessonRepository;

import java.util.Collection;

@Service
public class LessonService {

	private final LessonRepository lessonRepository;
	private final LessonConverter lessonConverter;
	private final CourseVersionRepository courseVersionRepository;

	@Autowired
	public LessonService(LessonRepository lessonRepository, LessonConverter lessonConverter,
						 CourseVersionRepository courseVersionRepository) {
		this.lessonRepository = lessonRepository;
		this.lessonConverter = lessonConverter;
		this.courseVersionRepository = courseVersionRepository;
	}

	public Collection<LessonDto> getAllCourseVersionLessons(Long courseId, Integer courseVersionNumber) {
		CourseVersion courseVersion = courseVersionRepository.findByCourse_IdAndVersionNumber(courseId, courseVersionNumber)
		.orElseThrow(() -> new CourseVersionNotFound(courseId, courseVersionNumber));

		return getAllCourseVersionLessons(courseVersion.getId());
	}

	public Collection<LessonDto> getAllCourseVersionLessons(long courseVersionId) {
		return lessonConverter.convertFromEntities(lessonRepository.findByCourseVersion_Id(courseVersionId));
	}

	public Long createLesson(LessonDto lessonDto) {
		Lesson lesson = lessonConverter.convertFromDto(lessonDto);
		lessonRepository.save(lesson);
		return lesson.getId();
	}

	public LessonDto getLessonById(Long id) {
		Lesson lesson = validateLesson(id);

		return lessonConverter.convertFromEntity(lesson);
	}

	public LessonDto updateLesson(Long id, LessonDto lessonDto) {
		validateLesson(id);
		lessonDto.setId(id);
		Lesson lesson = lessonConverter.convertFromDto(lessonDto);
		lessonRepository.save(lesson);

		return lessonDto;
	}

	private Lesson validateLesson(Long id) throws LessonNotFoundException {
		return lessonRepository.findById(id)
				.orElseThrow(() -> new LessonNotFoundException(id));
	}
}
