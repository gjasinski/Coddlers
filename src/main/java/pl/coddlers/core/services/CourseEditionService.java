package pl.coddlers.core.services;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.coddlers.core.exceptions.CourseEditionNotFoundException;
import pl.coddlers.core.models.converters.CourseEditionConverter;
import pl.coddlers.core.models.dto.CourseEditionDto;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.CourseEditionLesson;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.core.repositories.CourseEditionLessonRepository;
import pl.coddlers.core.repositories.CourseEditionRepository;
import pl.coddlers.core.repositories.LessonRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

@Service
public class CourseEditionService {
    private static final int INVITATION_LINK_LENGTH = 6;
    private final CourseEditionRepository courseEditionRepository;
    private final CourseEditionConverter courseEditionConverter;
    private final LessonRepository lessonRepository;
    private final CourseEditionLessonRepository courseEditionLessonRepository;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public CourseEditionService(UserDetailsServiceImpl userDetailsService, CourseEditionRepository courseEditionRepository, CourseEditionConverter courseEditionConverter, LessonRepository lessonRepository, CourseEditionLessonRepository courseEditionLessonRepository) {
        this.userDetailsService = userDetailsService;
        this.courseEditionRepository = courseEditionRepository;
        this.courseEditionConverter = courseEditionConverter;
        this.lessonRepository = lessonRepository;
        this.courseEditionLessonRepository = courseEditionLessonRepository;
    }

    public CourseEditionDto getCourseEditionById(Long id) {
        CourseEdition courseEdition = validateCourseEdition(id);
        return courseEditionConverter.convertFromEntity(courseEdition);
    }

    private CourseEdition validateCourseEdition(Long id) throws CourseEditionNotFoundException {
        return courseEditionRepository.findById(id).orElseThrow(() -> new CourseEditionNotFoundException(id));
    }

    public List<CourseEditionDto> getCourseEditionsByCourseVersionId(Long courseVersionId) {
        return courseEditionRepository.findAllByCourseVersionId(courseVersionId).stream()
                .map(courseEditionConverter::convertFromEntity)
                .collect(Collectors.toList());
    }

    public CourseEdition createCourseEdition(CourseEditionDto courseEditionDto) {
        return courseEditionRepository.save(courseEditionConverter.convertFromDto(courseEditionDto));

    }

    public List<CourseEditionLesson> cloneLessons(CourseEdition courseEdition) {
        List<Lesson> lessons = lessonRepository.findByCourseVersionId(courseEdition.getCourseVersion().getId());
        return lessons.stream()
                .map(lesson -> {
                    CourseEditionLesson courseEditionLesson = createCourseEditionLesson(courseEdition, lesson);
                    return courseEditionLessonRepository.save(courseEditionLesson);
                })
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
    public void addStudentToCourseEdition(String invitationLink) {
        CourseEdition courseEdition = courseEditionRepository.findByInvitationLink(invitationLink).orElseThrow(() -> new CourseEditionNotFoundException(invitationLink));
        User currentUser = userDetailsService.getCurrentUserEntity();
        courseEdition.getUsers().add(currentUser);
        courseEditionRepository.saveAndFlush(courseEdition);
    }

    public String getInvitationLink(Long courseEditionId) {
        CourseEdition courseEdition = courseEditionRepository.findById(courseEditionId).orElseThrow(() -> new CourseEditionNotFoundException(courseEditionId));
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(LETTERS, DIGITS)
                .build();
        String invitationLink = courseEdition.getInvitationLink();

        if (invitationLink == null) {
            do {
                invitationLink = generator.generate(INVITATION_LINK_LENGTH);
            } while (courseEditionRepository.findByInvitationLink(invitationLink).isPresent());

            courseEdition.setInvitationLink(invitationLink);
            courseEditionRepository.saveAndFlush(courseEdition);
        }
        return invitationLink;
    }

    private CourseEditionLesson createCourseEditionLesson(CourseEdition courseEdition, Lesson lesson) {
        CourseEditionLesson courseEditionLesson = new CourseEditionLesson();
        courseEditionLesson.setCourseEdition(courseEdition);
        courseEditionLesson.setLesson(lesson);
        courseEditionLesson.setStartDate(courseEdition.getStartDate());
        courseEditionLesson.setEndDate(getEndDate(courseEdition, lesson));
        return courseEditionLesson;
    }

    private Timestamp getEndDate(CourseEdition courseEdition, Lesson lesson) {
        LocalDateTime startDate = courseEdition.getStartDate().toLocalDateTime();
        LocalDateTime endTime = startDate.plusDays(lesson.getTimeInDays());
        return Timestamp.valueOf(endTime);
    }
}
