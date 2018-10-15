package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.models.converters.CourseVersionConverter;
import pl.coddlers.core.models.dto.CourseVersionDto;
import pl.coddlers.core.models.entity.CourseVersion;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.repositories.CourseVersionRepository;
import pl.coddlers.core.repositories.LessonRepository;

import java.util.Collection;
import java.util.List;

@Service
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

    public CourseVersion createCourseVersion(Long courseId) {
        return courseVersionRepository.findFirstByCourseIdOrderByVersionNumberDesc(courseId)
                .map((courseVersion) -> {
                    courseVersionRepository.save(createNewCourseVersion(courseVersion));
                    List<Lesson> allCourseVersionLessons = lessonRepository.findByCourseVersionId(courseVersion.getId());
                    allCourseVersionLessons.forEach(lesson -> lessonService.createNewVersionLesson(lesson, courseVersion));
                    return courseVersion;
                })
                .orElseThrow(() -> new IllegalArgumentException("Not found course version for courseId: " + courseId));
    }

    private CourseVersion createNewCourseVersion(CourseVersion courseVersion) {
        return new CourseVersion(incrementCourseVersionNumber(courseVersion), courseVersion.getCourse());
    }

    private int incrementCourseVersionNumber(CourseVersion courseVersion) {
        return courseVersion.getVersionNumber() + 1;
    }
}
