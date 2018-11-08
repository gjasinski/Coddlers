package pl.coddlers.core.models.converters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coddlers.core.exceptions.CourseVersionNotFound;
import pl.coddlers.core.exceptions.InternalServerErrorException;
import pl.coddlers.core.models.dto.LessonDto;
import pl.coddlers.core.models.entity.Course;
import pl.coddlers.core.models.entity.CourseVersion;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.repositories.CourseRepository;
import pl.coddlers.core.repositories.CourseVersionRepository;
import pl.coddlers.core.repositories.LessonRepository;

@Slf4j
@Component
public class LessonConverter implements BaseConverter<Lesson, LessonDto> {

	private final LessonRepository lessonRepository;
	private final CourseRepository courseRepository;
	private final CourseVersionRepository courseVersionRepository;

	@Autowired
	public LessonConverter(LessonRepository lessonRepository, CourseRepository courseRepository,
						   CourseVersionRepository courseVersionRepository) {
		this.lessonRepository = lessonRepository;
		this.courseRepository = courseRepository;
		this.courseVersionRepository = courseVersionRepository;
	}

	@Override
	public LessonDto convertFromEntity(Lesson entity) {
		LessonDto lessonDto = new LessonDto();
		lessonDto.setId(entity.getId());
		lessonDto.setTitle(entity.getTitle());
		lessonDto.setDescription(entity.getDescription());
		lessonDto.setWeight(entity.getWeight());
		lessonDto.setTimeInDays(entity.getTimeInDays());
		lessonDto.setCourseVersionNumber(entity.getCourseVersion().getVersionNumber());

		Long courseVersionId = entity.getCourseVersion().getId();
		Course course = courseRepository.findByCourseVersion_Id(courseVersionId).orElseThrow(
				() -> {
					log.error("Error occurs while attempt to get course entity by CourseVersionId = "+courseVersionId);
					return new InternalServerErrorException("Something went wrong while retrieving lessons from db. " +
							"Please contact with administrator.");
				}
		);

		lessonDto.setCourseId(course.getId());

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
		// lesson.setGitStudentProjectId(dto.getGitStudentProjectId());

		CourseVersion courseVersion = courseVersionRepository.findByCourse_IdAndVersionNumber(dto.getCourseId(), dto.getCourseVersionNumber())
				.orElseThrow(() -> new CourseVersionNotFound(dto.getCourseId(), dto.getCourseVersionNumber()));
		lesson.setCourseVersion(courseVersion);

		return lesson;
	}
}
