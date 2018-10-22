package pl.coddlers.core.services;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import pl.coddlers.mail.Mail;
import pl.coddlers.mail.MailInitializer;
import pl.coddlers.mail.MailScheduler;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

@Service
public class CourseEditionService {
    private static final int INVITATION_LINK_LENGTH = 6;
    private static final String INVITATION_FROM_EMAIL = "pl.coddlers.mail.invitationmail.from";
    private static final String INVITATION_TOKEN_PATH = "pl.coddlers.mail.invitationmail.path";
    private final CourseEditionRepository courseEditionRepository;
    private final CourseEditionConverter courseEditionConverter;
    private final LessonRepository lessonRepository;
    private final CourseEditionLessonRepository courseEditionLessonRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final Environment environment;

    @Autowired
    public CourseEditionService(UserDetailsServiceImpl userDetailsService, CourseEditionRepository courseEditionRepository, CourseEditionConverter courseEditionConverter, LessonRepository lessonRepository, CourseEditionLessonRepository courseEditionLessonRepository, Environment environment) {
        this.userDetailsService = userDetailsService;
        this.courseEditionRepository = courseEditionRepository;
        this.courseEditionConverter = courseEditionConverter;
        this.lessonRepository = lessonRepository;
        this.courseEditionLessonRepository = courseEditionLessonRepository;
        this.environment = environment;
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

    public List<CourseEditionLesson> createCourseEditionLessons(CourseEdition courseEdition) {
        List<Lesson> lessons = lessonRepository.findByCourseVersionId(courseEdition.getCourseVersion().getId());
        return lessons.stream()
                .map(lesson -> {
                    CourseEditionLesson courseEditionLesson = createCourseEditionLesson(courseEdition, lesson);
                    return courseEditionLessonRepository.save(courseEditionLesson);
                })
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
    public boolean addStudentToCourseEdition(String invitationToken) {
        CourseEdition courseEdition = courseEditionRepository.findByInvitationToken(invitationToken).orElseThrow(() -> new CourseEditionNotFoundException(invitationToken));
        User currentUser = userDetailsService.getCurrentUserEntity();
        if (courseEdition.getUsers().contains(currentUser))
            return false;
        courseEdition.getUsers().add(currentUser);
        courseEditionRepository.saveAndFlush(courseEdition);
        return true;
    }

    public String getInvitationLink(String host, Long courseEditionId) {
        CourseEdition courseEdition = courseEditionRepository.findById(courseEditionId).orElseThrow(() -> new CourseEditionNotFoundException(courseEditionId));
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(LETTERS, DIGITS)
                .build();
        String invitationToken = courseEdition.getInvitationToken();

        if (invitationToken == null) {
            do {
                invitationToken = generator.generate(INVITATION_LINK_LENGTH);
            } while (courseEditionRepository.findByInvitationToken(invitationToken).isPresent());

            courseEdition.setInvitationToken(invitationToken);
            courseEditionRepository.saveAndFlush(courseEdition);
        }
        return host + environment.getProperty(INVITATION_TOKEN_PATH) + invitationToken;
    }

    public void sendInvitationLinkByMail(String invitationLink, List<InternetAddress> students) throws AddressException {
        MailInitializer mailInitializer = new MailInitializer();
        MailScheduler mailScheduler = mailInitializer.initialize();

        // TODO emailTitle and htmlMessage should be configured to fulfill clients expectations
        String invitationToken = getInvitationTokenFromInvitationLink(invitationLink);
        CourseEdition courseEdition = courseEditionRepository.findByInvitationToken(invitationToken).orElseThrow(() -> new CourseEditionNotFoundException(invitationToken));
        String emailTitle = "Invitation to course \"" + courseEdition.getTitle() + "\" on Coddlers.pl";
        String htmlMessage = "You have been invited to course \"" + courseEdition.getTitle() + "\" on www.Coddlers.pl. Click on link below to join the course!<br>" + invitationLink;

        InternetAddress from = new InternetAddress(Objects.requireNonNull(environment.getProperty(INVITATION_FROM_EMAIL)));
        Mail mail = new Mail(from, students, emailTitle, htmlMessage);
        mailScheduler.scheduleMail(mail);
    }

    private String getInvitationTokenFromInvitationLink(String invitationLink) {
        String[] invitationLinkSplitted = invitationLink.split("=");
        return invitationLinkSplitted[invitationLinkSplitted.length - 1];
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
