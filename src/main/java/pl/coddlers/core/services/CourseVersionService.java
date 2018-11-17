package pl.coddlers.core.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.GitAsynchronousOperationException;
import pl.coddlers.core.models.converters.CourseVersionConverter;
import pl.coddlers.core.models.dto.CourseVersionDto;
import pl.coddlers.core.models.dto.LessonDto;
import pl.coddlers.core.models.entity.CourseVersion;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.core.repositories.CourseVersionRepository;
import pl.coddlers.core.repositories.LessonRepository;
import pl.coddlers.git.models.event.ProjectDto;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class CourseVersionService {

    private CourseVersionRepository courseVersionRepository;
    private CourseVersionConverter courseVersionConverter;
    private LessonService lessonService;
    private LessonRepository lessonRepository;

    @Autowired
    public CourseVersionService(CourseVersionRepository courseVersionRepository,
                                CourseVersionConverter courseVersionConverter, LessonService lessonService, LessonRepository lessonRepository) {
        this.courseVersionRepository = courseVersionRepository;
        this.courseVersionConverter = courseVersionConverter;
        this.lessonService = lessonService;
        this.lessonRepository = lessonRepository;
    }

    public Collection<CourseVersionDto> getCourseVersionsByCourseId(Long courseId) {
        return courseVersionConverter
                .convertFromEntities(courseVersionRepository
                        .findByCourse_IdOrderByVersionNumberDesc(courseId)
                );
    }

    public CourseVersionDto createCourseVersion(Long courseId) {
        return courseVersionRepository.findFirstByCourseIdOrderByVersionNumberDesc(courseId)
                .map((courseVersion) -> {
                    CourseVersion newCourseVersion = courseVersionRepository.save(createNewCourseVersion(courseVersion));
                    lessonRepository.findByCourseVersionId(courseVersion.getId())
                            .stream()
                            .map(lesson -> lessonService.createNewVersionLesson(lesson, newCourseVersion)
                                    .exceptionally(e -> logAndWrapException(lesson, e))
                            )
                            .forEach(CompletableFuture::join);
                    return courseVersionConverter.convertFromEntity(newCourseVersion);
                })
                .orElseThrow(() -> new IllegalArgumentException("Not found course version for courseId: " + courseId));
    }

    private Lesson logAndWrapException(Lesson lesson, Throwable v) {
        String exceptionMsg = String.format("Cannot create new version of lesson for lesson: %s, ", lesson);
        log.error(exceptionMsg, v);
        throw new GitAsynchronousOperationException(exceptionMsg, v);
    }

    private CourseVersion createNewCourseVersion(CourseVersion courseVersion) {
        return new CourseVersion(incrementCourseVersionNumber(courseVersion), courseVersion.getCourse());
    }

    private int incrementCourseVersionNumber(CourseVersion courseVersion) {
        return courseVersion.getVersionNumber() + 1;
    }
}
