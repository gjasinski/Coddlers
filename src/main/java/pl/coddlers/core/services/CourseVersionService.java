package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.models.converters.CourseVersionConverter;
import pl.coddlers.core.models.dto.CourseVersionDto;
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
}
