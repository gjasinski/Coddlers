package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.CourseEditionNotFoundException;
import pl.coddlers.core.models.converters.CourseEditionConverter;
import pl.coddlers.core.models.dto.CourseEditionDto;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.core.repositories.CourseEditionRepository;
import pl.coddlers.core.repositories.UserDataRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseEditionService {

    private final CourseEditionRepository courseEditionRepository;

    private final CourseEditionConverter courseEditionConverter;

    private final UserDetailsServiceImpl userDetailsService;

    private final UserDataRepository userDataRepository;

    @Autowired
    public CourseEditionService(CourseEditionRepository courseEditionRepository, CourseEditionConverter courseEditionConverter, UserDetailsServiceImpl userDetailsService, UserDataRepository userDataRepository) {
        this.courseEditionRepository = courseEditionRepository;
        this.courseEditionConverter = courseEditionConverter;
        this.userDetailsService = userDetailsService;
        this.userDataRepository = userDataRepository;
    }

    public CourseEditionDto getCourseEditionById(Long id) {
        CourseEdition courseEdition = validateCourseEdition(id);
        return courseEditionConverter.convertFromEntity(courseEdition);
    }

    private CourseEdition validateCourseEdition(Long id) throws CourseEditionNotFoundException {
        return courseEditionRepository.findById(id).orElseThrow(() -> new CourseEditionNotFoundException(id));
    }

    public List<CourseEditionDto> getCourses() {
        Long userId = userDetailsService.getCurrentUserEntity().getId();
        Optional<User> user = userDataRepository.findById(userId);
        if (user.isPresent()) {
            return user.get().getCourseEditions()
                    .stream()
                    .map(courseEditionConverter::convertFromEntity)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Logged user does not exists");
        }
    }
}
