package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.models.converters.CourseVersionConverter;
import pl.coddlers.core.models.dto.CourseVersionDto;
import pl.coddlers.core.models.entity.CourseVersion;
import pl.coddlers.core.repositories.CourseVersionRepository;

import java.util.Collection;

@Service
public class CourseVersionService {

    private CourseVersionRepository courseVersionRepository;
    private CourseVersionConverter courseVersionConverter;

    @Autowired
    public CourseVersionService(CourseVersionRepository courseVersionRepository,
                                CourseVersionConverter courseVersionConverter) {
        this.courseVersionRepository = courseVersionRepository;
        this.courseVersionConverter = courseVersionConverter;
    }

    public Collection<CourseVersionDto> getCourseVersionsByCourseId(Long courseId) {
        return courseVersionConverter
                .convertFromEntities(courseVersionRepository
                        .findByCourse_IdOrderByVersionNumberDesc(courseId)
                );
    }

    public CourseVersion createCourseVersion(Long courseId) {
        return courseVersionRepository.findFirstByCourseIdOrderByVersionNumberDesc(courseId)
                .map((courseVersion) -> courseVersionRepository.save(createNewCourseVersion(courseVersion)))
                .orElseThrow(() -> new IllegalArgumentException("Not found course version for courseId: " + courseId));
    }

    private CourseVersion createNewCourseVersion(CourseVersion courseVersion) {
        return new CourseVersion(incrementCourseVersionNumber(courseVersion), courseVersion.getCourse());
    }

    private int incrementCourseVersionNumber(CourseVersion courseVersion) {
        return courseVersion.getVersionNumber() + 1;
    }
}
