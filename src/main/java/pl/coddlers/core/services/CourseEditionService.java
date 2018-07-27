package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.CourseEditionNotFoundException;
import pl.coddlers.core.models.converters.CourseEditionConverter;
import pl.coddlers.core.models.dto.CourseEditionDto;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.repositories.CourseEditionRepository;

@Service
public class CourseEditionService {

    private final CourseEditionRepository courseEditionRepository;

    private final CourseEditionConverter courseEditionConverter;

    @Autowired
    public CourseEditionService(CourseEditionRepository courseEditionRepository, CourseEditionConverter courseEditionConverter) {
        this.courseEditionRepository = courseEditionRepository;
        this.courseEditionConverter = courseEditionConverter;
    }

    public CourseEditionDto getCourseEditionById(Long id) {
        CourseEdition courseEdition = validateCourseEdition(id);
        return courseEditionConverter.convertFromEntity(courseEdition);
    }

    private CourseEdition validateCourseEdition(Long id) throws CourseEditionNotFoundException {
        return courseEditionRepository.findById(id).orElseThrow(() -> new CourseEditionNotFoundException(id));
    }
}
