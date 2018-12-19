package pl.coddlers.core.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.coddlers.core.exceptions.CourseEditionLessonNotFoundException;
import pl.coddlers.core.exceptions.CourseEditionNotFoundException;
import pl.coddlers.core.exceptions.GitAsynchronousOperationException;
import pl.coddlers.core.models.converters.CourseEditionConverter;
import pl.coddlers.core.models.converters.CourseEditionLessonConverter;
import pl.coddlers.core.models.dto.CourseDto;
import pl.coddlers.core.models.dto.CourseEditionDto;
import pl.coddlers.core.models.dto.CourseEditionLessonDto;
import pl.coddlers.core.models.dto.CourseWithCourseEditionDto;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.CourseEditionLesson;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.core.models.dto.InvitationLinkDto;
import pl.coddlers.core.repositories.CourseEditionLessonRepository;
import pl.coddlers.core.repositories.CourseEditionRepository;
import pl.coddlers.core.repositories.LessonRepository;
import pl.coddlers.core.repositories.StudentLessonRepositoryRepository;
import pl.coddlers.git.services.GitGroupService;
import pl.coddlers.mail.Mail;
import pl.coddlers.mail.MailInitializer;
import pl.coddlers.mail.MailScheduler;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

@Slf4j
@Service
public class CourseEditionService {
    private static final int INVITATION_LINK_LENGTH = 6;
    private static final String INVITATION_FROM_EMAIL = "pl.coddlers.mail.invitationmail.from";
    private static final String INVITATION_TOKEN_PATH = "pl.coddlers.mail.invitationmail.path";

    private final CourseEditionConverter courseEditionConverter;
    private final CourseEditionRepository courseEditionRepository;
    private final CourseEditionLessonRepository courseEditionLessonRepository;
    private final StudentLessonRepositoryRepository studentLessonRepositoryRepository;

    private final CourseEditionLessonConverter courseEditionLessonConverter;
    private final GitGroupService gitGroupService;

    private final UserDetailsServiceImpl userDetailsService;
    private final Environment environment;
    private final LessonRepository lessonRepository;
    private final SubmissionService submissionService;
    private final CourseService courseService;
    private final LessonService lessonService;
    private final CourseEditionLessonService courseEditionLessonService;

    @Autowired
    public CourseEditionService(CourseEditionRepository courseEditionRepository, CourseEditionConverter courseEditionConverter,
                                LessonRepository lessonRepository, CourseEditionLessonRepository courseEditionLessonRepository,
                                GitGroupService gitGroupService, UserDetailsServiceImpl userDetailsService,
                                SubmissionService submissionService, CourseService courseService, Environment environment,
                                CourseEditionLessonConverter courseEditionLessonConverter, LessonService lessonService,
                                StudentLessonRepositoryRepository studentLessonRepositoryRepository, CourseEditionLessonService courseEditionLessonService) {
        this.courseEditionRepository = courseEditionRepository;
        this.courseEditionConverter = courseEditionConverter;
        this.lessonRepository = lessonRepository;
        this.courseEditionLessonRepository = courseEditionLessonRepository;
        this.environment = environment;
        this.submissionService = submissionService;
        this.courseService = courseService;
        this.courseEditionLessonConverter = courseEditionLessonConverter;
        this.gitGroupService = gitGroupService;
        this.userDetailsService = userDetailsService;
        this.lessonService = lessonService;
        this.studentLessonRepositoryRepository = studentLessonRepositoryRepository;
        this.courseEditionLessonService = courseEditionLessonService;
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
        try {
            CourseEdition courseEdition = courseEditionRepository.save(courseEditionConverter.convertFromDto(courseEditionDto));
            Long gitGroupId = gitGroupService.createGroup(createGroupName(courseEditionDto, courseEdition.getId())).get().getId();
            courseEdition.setGitGroupId(gitGroupId);
            courseEditionRepository.save(courseEdition);
            addTeachersToGroup(gitGroupId);
            return courseEdition;
        } catch (InterruptedException | ExecutionException e) {
            log.error(String.format("Cannot create new course edition for %s", courseEditionDto.toString()), e);
            throw new GitAsynchronousOperationException("Cannot create new course edition");
        }
    }

    private void addTeachersToGroup(Long gitGroupId) {
        User currentUser = userDetailsService.getCurrentUserEntity();
        gitGroupService.addUserToGroupAsMaintainer(currentUser.getGitUserId(), gitGroupId);
        // TODO: 20.10.18 add all teacher to group from database
    }

    private String createGroupName(CourseEditionDto courseEditionDto, Long courseEditionId) {
        Long courseVersionId = courseEditionDto.getCourseVersion().getId();
        return courseVersionId + "_" + courseEditionId + "_" + Long.toString(Instant.now().getEpochSecond());
    }

    public List<CourseEditionLesson> createCourseEditionLessons(CourseEdition courseEdition) {
        List<Lesson> lessons = lessonRepository.findByCourseVersionId(courseEdition.getCourseVersion().getId());
        List<CourseEditionLesson> courseEditionLessons = new ArrayList<>();
        Timestamp startDate = courseEdition.getStartDate();

        for (Lesson lesson : lessons) {
            CourseEditionLesson courseEditionLesson = courseEditionLessonService.createCourseEditionLesson(courseEdition, lesson, startDate);
            courseEditionLessons.add(courseEditionLesson);
            startDate = courseEditionLessonService.addDaysToDate(courseEditionLesson.getEndDate(), 1);
        }
        return courseEditionLessons;
    }

    public Collection<CourseEditionLessonDto> getCourseEditionLessonList(Long editionId) {
        return courseEditionLessonConverter.convertFromEntities(
                courseEditionLessonRepository.findByCourseEdition_Id(editionId)
        );
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
    public boolean addStudentToCourseEdition(String invitationToken) {
        CourseEdition courseEdition = courseEditionRepository.findByInvitationToken(invitationToken)
                .orElseThrow(() -> new CourseEditionNotFoundException(invitationToken));
        User currentUser = userDetailsService.getCurrentUserEntity();
        if (courseEdition.getUsers().contains(currentUser))
            return false;
        courseEdition.getUsers().add(currentUser);
        courseEditionRepository.saveAndFlush(courseEdition);

        courseEdition.getCourseEditionLesson().stream()
                .filter(courseEditionLesson -> courseEditionLesson.getStartDate().before(new Timestamp(System.currentTimeMillis())))
                .forEach(courseEditionLesson ->
                        lessonService.forkModelLessonForUser(courseEdition, courseEditionLesson.getLesson(), currentUser)
//                                .thenAccept(studentLessonRepositoryRepository::save)
                                .exceptionally(ex -> {
                                    log.error(String.format("Cannot fork lesson: %s from course edition %s for user: %s", courseEditionLesson.getLesson().toString(), courseEdition.toString(), currentUser.toString()), ex);
                                    return null;
                                }));
        return true;
    }

    public InvitationLinkDto getInvitationLink(String host, Long courseEditionId) {
        host = addHttpWwwToInvitationLink(host);
        CourseEdition courseEdition = courseEditionRepository.findById(courseEditionId)
                .orElseThrow(() -> new CourseEditionNotFoundException(courseEditionId));
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
        return new InvitationLinkDto(host + environment.getProperty(INVITATION_TOKEN_PATH) + invitationToken);
    }

    private String addHttpWwwToInvitationLink(String host) {
        if(!host.toLowerCase().startsWith("localhost")) {
            if (!host.toLowerCase().startsWith("http://www.") && !host.toLowerCase().startsWith("www.")) {
                host = "http://www." + host;
            } else if (host.toLowerCase().startsWith("www.")) {
                host = "http://" + host;
            }
        }
        return host;
    }

    public void sendInvitationLinkByMail(String invitationLink, List<InternetAddress> students) throws AddressException {
        MailInitializer mailInitializer = new MailInitializer();
        MailScheduler mailScheduler = mailInitializer.initialize();

        // TODO emailTitle and htmlMessage should be configured to fulfill clients expectations
        String invitationToken = getInvitationTokenFromInvitationLink(invitationLink);
        CourseEdition courseEdition = courseEditionRepository.findByInvitationToken(invitationToken)
                .orElseThrow(() -> new CourseEditionNotFoundException(invitationToken));
        String emailTitle = "Invitation to course \"" + courseEdition.getTitle() + "\" on Coddlers.pl";
        String htmlMessage = "You have been invited to course \"" + courseEdition.getTitle() + "\" on www.Coddlers.pl. " +
                "Click on link below to join the course!<br>" + invitationLink;

        InternetAddress from = new InternetAddress(Objects.requireNonNull(environment.getProperty(INVITATION_FROM_EMAIL)));
        Mail mail = new Mail(from, students, emailTitle, htmlMessage);
        mailScheduler.scheduleMail(mail);
    }

    private String getInvitationTokenFromInvitationLink(String invitationLink) {
        String[] invitationLinkSplitted = invitationLink.split("=");
        return invitationLinkSplitted[invitationLinkSplitted.length - 1];
    }

    public CourseEditionLessonDto getCourseEditionLesson(Long lessonId, Long editionId) {
        return courseEditionLessonConverter.convertFromEntity(
                courseEditionLessonRepository.findByLesson_IdAndCourseEdition_Id(lessonId, editionId)
                        .orElseThrow(() -> new CourseEditionLessonNotFoundException(lessonId, editionId))
        );
    }

    public void updateCourseEditionLesson(Long id, CourseEditionLessonDto courseEditionLessonDto) {
        courseEditionLessonDto.setId(id);
        CourseEditionLesson courseEditionLesson = courseEditionLessonConverter.convertFromDto(courseEditionLessonDto);
        courseEditionLessonRepository.save(courseEditionLesson);
    }

    public List<CourseWithCourseEditionDto> getAllEditionsWithEnrolledStudent(User user) {
        return courseEditionRepository.findAllCourseEditionWithEnrolledStudent(user.getId())
                .stream()
                .map(courseEditionConverter::convertFromEntity)
                .map(mapEditionToCourseWithCourseEditionDto(user))
                .collect(Collectors.toList());
    }

    private Function<CourseEditionDto, CourseWithCourseEditionDto> mapEditionToCourseWithCourseEditionDto(User currentUser) {
        return edition -> {
            Long courseVersionId = edition.getCourseVersion().getId();
            CourseDto courseDto = courseService.getCourseByCourseVersionId(courseVersionId)
                    .orElseThrow(() -> new IllegalStateException(exceptionMessage(edition)));
            CourseEdition courseEdition = courseEditionRepository.getOne(edition.getId());
            int allTasks = submissionService.countAllTask(currentUser, courseEdition);
            int gradedTasks = submissionService.countAllGradedTasks(currentUser, courseEdition);
            int submittedTasks = submissionService.countAllSubmittedTasks(currentUser, courseEdition);
            int gradedLessons = submissionService.countGradedLessonStatus(currentUser, courseEdition);
            int submittedLessons = submissionService.countSubmittedLessonStatus(currentUser, courseEdition);
            int lessonsSize = lessonRepository.getLessonsByCourseEditionId(courseEdition.getId()).size();

            return new CourseWithCourseEditionDto(courseDto, edition, submittedTasks, gradedTasks, allTasks,
                    gradedLessons, submittedLessons, lessonsSize);
        };
    }

    private String exceptionMessage(CourseEditionDto edition) {
        return String.format("Inconsistency on database, didn't found Course for CourseEdition: %s", edition.toString());
    }
}
