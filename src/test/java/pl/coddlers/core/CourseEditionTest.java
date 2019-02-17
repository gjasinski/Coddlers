package pl.coddlers.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.coddlers.core.exceptions.CourseEditionLessonNotFoundException;
import pl.coddlers.core.exceptions.CourseEditionNotFoundException;
import pl.coddlers.core.exceptions.LessonNotFoundException;
import pl.coddlers.core.models.converters.CourseEditionConverter;
import pl.coddlers.core.models.converters.CourseEditionLessonConverter;
import pl.coddlers.core.models.dto.*;
import pl.coddlers.core.models.entity.*;
import pl.coddlers.core.repositories.CourseEditionLessonRepository;
import pl.coddlers.core.repositories.CourseEditionRepository;
import pl.coddlers.core.repositories.LessonRepository;
import pl.coddlers.core.services.*;
import pl.coddlers.git.models.event.ProjectDto;
import pl.coddlers.git.services.GitGroupService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CourseEditionTest {
    @Mock
    private CourseEditionRepository courseEditionRepositoryMock;
    @Mock
    private CourseEditionConverter courseEditionConverterMock;
    @Mock
    private CourseEditionLessonRepository courseEditionLessonRepositoryMock;
    @Mock
    private CourseEditionLessonConverter courseEditionLessonConverterMock;
    @Mock
    private GitGroupService gitGroupServiceMock;
    @Mock
    private UserDetailsServiceImpl userDetailsServiceMock;
    @Mock
    private LessonRepository lessonRepositoryMock;
    @Mock
    private CourseEditionLessonService courseEditionLessonServiceMock;
    @Mock
    private LessonService lessonServiceMock;
    @Mock
    private CourseService courseServiceMock;
    @Mock
    private SubmissionService submissionServiceMock;

    @InjectMocks
    private CourseEditionService courseEditionService;

    private final long COURSE_EDITION_ID = 1;

    @Test
    public void getCourseEditionByIdTest() {
        Optional<CourseEdition> courseEdition = Optional.of(new CourseEdition());
        CourseEditionDto courseEditionDto = new CourseEditionDto();

        when(courseEditionRepositoryMock.findById(COURSE_EDITION_ID)).thenReturn(courseEdition);
        when(courseEditionConverterMock.convertFromEntity(courseEdition.get())).thenReturn(courseEditionDto);
        CourseEditionDto courseEditionDtoResult = courseEditionService.getCourseEditionById(COURSE_EDITION_ID);

        assertThat(courseEditionDtoResult).isEqualTo(courseEditionDto);
    }

    @Test(expected = CourseEditionNotFoundException.class)
    public void getCourseEditionByIdThrowsCourseEditionNotFoundExceptionTest() {
        when(courseEditionRepositoryMock.findById(COURSE_EDITION_ID)).thenThrow(new CourseEditionNotFoundException(COURSE_EDITION_ID));
        courseEditionService.getCourseEditionById(COURSE_EDITION_ID);
    }

    @Test
    public void getCourseEditionsByCourseVersionIdTest() {
        long courseVersionId = 1;
        CourseEdition courseEdition1 = new CourseEdition();
        CourseEdition courseEdition2 = new CourseEdition();
        CourseEdition courseEdition3 = new CourseEdition();
        CourseEditionDto courseEditionDto1 = new CourseEditionDto();
        CourseEditionDto courseEditionDto2 = new CourseEditionDto();
        CourseEditionDto courseEditionDto3 = new CourseEditionDto();
        List<CourseEdition> courseEditions = Arrays.asList(courseEdition1, courseEdition2, courseEdition3);

        when(courseEditionConverterMock.convertFromEntity(courseEdition1)).thenReturn(courseEditionDto1);
        when(courseEditionConverterMock.convertFromEntity(courseEdition2)).thenReturn(courseEditionDto2);
        when(courseEditionConverterMock.convertFromEntity(courseEdition3)).thenReturn(courseEditionDto3);
        when(courseEditionRepositoryMock.findAllByCourseVersionId(courseVersionId)).thenReturn(courseEditions);
        List<CourseEditionDto> courseEditionDtoList = courseEditionService.getCourseEditionsByCourseVersionId(courseVersionId);

        assertThat(courseEditionDtoList).isEqualTo(courseEditionDtoList);
    }

    @Test
    public void createCourseEditionTest() {
        CourseVersionDto courseVersionDto = new CourseVersionDto();
        CourseEditionDto courseEditionDto = new CourseEditionDto();
        courseEditionDto.setCourseVersion(courseVersionDto);
        CourseEdition courseEdition = new CourseEdition();
        long projectId = 1;
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(projectId);
        CompletableFuture<ProjectDto> projectDtoFuture = CompletableFuture.completedFuture(projectDto);

        when(courseEditionConverterMock.convertFromDto(courseEditionDto)).thenReturn(courseEdition);
        when(courseEditionRepositoryMock.save(courseEdition)).thenReturn(courseEdition);
        when(gitGroupServiceMock.createGroup(any(String.class))).thenReturn(projectDtoFuture);
        when(userDetailsServiceMock.getCurrentUserEntity()).thenReturn(new User());
        CourseEdition courseEditionResult = courseEditionService.createCourseEdition(courseEditionDto);

        verify(courseEditionRepositoryMock, times(2)).save(courseEdition);
        assertThat(courseEditionResult).isNotEqualTo(courseEditionDto);
        assertThat(courseEditionResult.getGitGroupId()).isEqualTo(projectId);
    }

    @Test
    public void createCourseEditionLessonsTest() {
        CourseEdition courseEdition = new CourseEdition();
        courseEdition.setStartDate(new Timestamp(System.currentTimeMillis()));
        CourseVersion courseVersion = new CourseVersion();
        courseEdition.setCourseVersion(courseVersion);
        int lesson1TimeInDays = 2;
        int lesson2TimeInDays = 3;
        int lesson3TimeInDays = 4;
        Lesson lesson1 = new Lesson();
        lesson1.setTimeInDays(lesson1TimeInDays);
        Lesson lesson2 = new Lesson();
        lesson2.setTimeInDays(lesson2TimeInDays);
        Lesson lesson3 = new Lesson();
        lesson3.setTimeInDays(lesson3TimeInDays);
        List<Lesson> lessons = Arrays.asList(lesson1, lesson2, lesson3);
        CourseEditionLesson courseEditionLesson1 = new CourseEditionLesson();
        courseEditionLesson1.setStartDate(courseEdition.getStartDate());
        LocalDateTime endDate = courseEditionLesson1.getStartDate().toLocalDateTime().plusDays(lesson1TimeInDays).plusDays(1);
        courseEditionLesson1.setEndDate(Timestamp.valueOf(endDate));
        CourseEditionLesson courseEditionLesson2 = new CourseEditionLesson();
        endDate = endDate.plusDays(lesson2TimeInDays).plusDays(1);
        courseEditionLesson2.setEndDate(Timestamp.valueOf(endDate));
        CourseEditionLesson courseEditionLesson3 = new CourseEditionLesson();
        endDate = endDate.plusDays(lesson3TimeInDays).plusDays(1);
        courseEditionLesson3.setEndDate(Timestamp.valueOf(endDate));

        when(lessonRepositoryMock.findByCourseVersionId(any())).thenReturn(lessons);
        when(courseEditionLessonServiceMock.createCourseEditionLesson(courseEdition, lesson1, courseEditionLesson1.getStartDate())).thenReturn(courseEditionLesson1);
        when(courseEditionLessonServiceMock.createCourseEditionLesson(courseEdition, lesson2, courseEditionLesson2.getStartDate())).thenReturn(courseEditionLesson2);
        when(courseEditionLessonServiceMock.createCourseEditionLesson(courseEdition, lesson3, courseEditionLesson3.getStartDate())).thenReturn(courseEditionLesson3);
        List<CourseEditionLesson> courseEditionLessonListResult = courseEditionService.createCourseEditionLessons(courseEdition);

        assertThat(courseEditionLessonListResult).contains(courseEditionLesson1).contains(courseEditionLesson2).contains(courseEditionLesson3);
    }

    @Test
    public void getCourseEditionLessonListTest() {
        List<CourseEditionLesson> courseEditionLessonList = Arrays.asList(new CourseEditionLesson(), new CourseEditionLesson(), new CourseEditionLesson());
        List<CourseEditionLessonDto> courseEditionLessonDtoList = Arrays.asList(new CourseEditionLessonDto(), new CourseEditionLessonDto(), new CourseEditionLessonDto());

        when(courseEditionLessonRepositoryMock.findByCourseEdition_Id(COURSE_EDITION_ID)).thenReturn(courseEditionLessonList);
        when(courseEditionLessonConverterMock.convertFromEntities(courseEditionLessonList)).thenReturn(courseEditionLessonDtoList);
        Collection<CourseEditionLessonDto> courseEditionDtoListResult = courseEditionService.getCourseEditionLessonList(COURSE_EDITION_ID);

        assertThat(courseEditionDtoListResult).isEqualTo(courseEditionLessonDtoList);
    }

    @Test
    public void addStudentToCourseEditionShouldReturnFalseTest() {
        String invitationToken = "Token";
        Optional<CourseEdition> courseEdition = Optional.of(new CourseEdition());
        User user = new User();
        courseEdition.get().setUsers(new HashSet<>(Collections.singletonList(user)));

        when(courseEditionRepositoryMock.findByInvitationToken(invitationToken)).thenReturn(courseEdition);
        when(userDetailsServiceMock.getCurrentUserEntity()).thenReturn(user);
        boolean result = courseEditionService.addStudentToCourseEdition(invitationToken);

        assertThat(result).isFalse();
    }

    @Test
    public void addStudentToCourseEditionNoLessonsToForkShouldReturnTrueTest() {
        String invitationToken = "Token";
        Optional<CourseEdition> courseEdition = Optional.of(new CourseEdition());
        courseEdition.get().setCourseEditionLesson(new ArrayList<>());
        User user = new User();

        when(courseEditionRepositoryMock.findByInvitationToken(invitationToken)).thenReturn(courseEdition);
        when(userDetailsServiceMock.getCurrentUserEntity()).thenReturn(user);
        boolean result = courseEditionService.addStudentToCourseEdition(invitationToken);

        verify(courseEditionRepositoryMock, times(1)).saveAndFlush(courseEdition.get());
        assertThat(result).isTrue();
    }

    @Test
    public void addStudentToCourseEditionShouldReturnTrueTest() {
        String invitationToken = "Token";
        Optional<CourseEdition> courseEdition = Optional.of(new CourseEdition());
        CourseEditionLesson courseEditionLesson = new CourseEditionLesson();
        courseEditionLesson.setStartDate(new Timestamp(System.currentTimeMillis()));
        courseEdition.get().setCourseEditionLesson(Collections.singletonList(courseEditionLesson));
        User user = new User();

        when(courseEditionRepositoryMock.findByInvitationToken(invitationToken)).thenReturn(courseEdition);
        when(userDetailsServiceMock.getCurrentUserEntity()).thenReturn(user);
        when(lessonServiceMock.forkModelLessonForUser(courseEdition.get(), courseEditionLesson.getLesson(), user)).thenReturn(CompletableFuture.completedFuture(new StudentLessonRepository()));
        boolean result = courseEditionService.addStudentToCourseEdition(invitationToken);

        verify(lessonServiceMock, times(1)).forkModelLessonForUser(courseEdition.get(), courseEditionLesson.getLesson(), user);
        verify(courseEditionRepositoryMock, times(1)).saveAndFlush(courseEdition.get());
        assertThat(result).isTrue();
    }

    @Test(expected = CourseEditionNotFoundException.class)
    public void addStudentToCourseEditionThrowsCourseEditionNotFoundExceptionTest() {
        String invitationToken = "Token";

        when(courseEditionRepositoryMock.findByInvitationToken(invitationToken)).thenThrow(new CourseEditionNotFoundException(invitationToken));
        courseEditionService.addStudentToCourseEdition(invitationToken);
    }

    @Test
    public void getInvitationLinkTest() {

    }

    @Test(expected = CourseEditionNotFoundException.class)
    public void getInvitationLinkThrowsCourseEditionNotFoundExceptionTest() {
        String host = "host";

        when(courseEditionRepositoryMock.findById(COURSE_EDITION_ID)).thenThrow(new CourseEditionNotFoundException(COURSE_EDITION_ID));
        courseEditionService.getInvitationLink(host, COURSE_EDITION_ID);
    }

    @Test
    public void sendInvitationLinkByMailTest() {

    }

    @Test(expected = CourseEditionNotFoundException.class)
    public void sendInvitationLinkByMailThrowsCourseEditionNotFoundExceptionTest() {
        String invitationLink = "invitationLink";

        when(courseEditionRepositoryMock.findByInvitationToken(invitationLink)).thenThrow(new CourseEditionNotFoundException(invitationLink));
        courseEditionService.sendInvitationLinkByMail(invitationLink, new ArrayList<>());
    }

    @Test
    public void getCourseEditionLessonTest() {
        long lessonId = 1;
        Optional<CourseEditionLesson> courseEditionLesson = Optional.of(new CourseEditionLesson());
        CourseEditionLessonDto courseEditionLessonDto = new CourseEditionLessonDto();

        when(courseEditionLessonConverterMock.convertFromEntity(courseEditionLesson.get())).thenReturn(courseEditionLessonDto);
        when(courseEditionLessonRepositoryMock.findByLesson_IdAndCourseEdition_Id(lessonId, COURSE_EDITION_ID)).thenReturn(courseEditionLesson);
        CourseEditionLessonDto courseEditionLessonDtoResult = courseEditionService.getCourseEditionLesson(lessonId, COURSE_EDITION_ID);

        assertThat(courseEditionLessonDtoResult).isEqualTo(courseEditionLessonDto);
    }

    @Test(expected = CourseEditionLessonNotFoundException.class)
    public void getCourseEditionLessonThrowsCourseEditionLessonNotFoundExceptionTest() {
        long lessonId = 1;

        when(courseEditionLessonRepositoryMock.findByLesson_IdAndCourseEdition_Id(lessonId, COURSE_EDITION_ID)).thenThrow(new CourseEditionLessonNotFoundException(lessonId, COURSE_EDITION_ID));
        courseEditionService.getCourseEditionLesson(lessonId, COURSE_EDITION_ID);
    }

    @Test
    public void updateCourseEditionLessonTest() {
        CourseEditionLessonDto courseEditionLessonDto = new CourseEditionLessonDto();

        courseEditionService.updateCourseEditionLesson(COURSE_EDITION_ID, courseEditionLessonDto);

        verify(courseEditionLessonConverterMock, times(1)).convertFromDto(courseEditionLessonDto);
        verify(courseEditionLessonRepositoryMock, times(1)).save(any(CourseEditionLesson.class));
    }

    @Test(expected = LessonNotFoundException.class)
    public void updateCourseEditionLessonThrowsLessonNotFoundExceptionTest() {
        long lessonId = 1;
        CourseEditionLessonDto courseEditionLessonDto = new CourseEditionLessonDto();

        when(courseEditionLessonConverterMock.convertFromDto(courseEditionLessonDto)).thenThrow(new LessonNotFoundException(lessonId));
        courseEditionService.updateCourseEditionLesson(COURSE_EDITION_ID, courseEditionLessonDto);
    }

    @Test(expected = CourseEditionNotFoundException.class)
    public void updateCourseEditionLessonThrowsCourseEditionNotFoundExceptionTest() {
        CourseEditionLessonDto courseEditionLessonDto = new CourseEditionLessonDto();

        when(courseEditionLessonConverterMock.convertFromDto(courseEditionLessonDto)).thenThrow(new CourseEditionNotFoundException(COURSE_EDITION_ID));
        courseEditionService.updateCourseEditionLesson(COURSE_EDITION_ID, courseEditionLessonDto);
    }

    @Test
    public void getAllEditionsWithEnrolledStudentTest() {
        User user = new User();
        CourseEdition courseEdition = new CourseEdition();
        CourseEditionDto courseEditionDto = new CourseEditionDto();
        CourseVersionDto courseVersionDto = new CourseVersionDto();
        courseEditionDto.setCourseVersion(courseVersionDto);
        Optional<CourseDto> courseDto = Optional.of(new CourseDto());

        when(courseEditionRepositoryMock.findAllCourseEditionWithEnrolledStudent(any())).thenReturn(Collections.singletonList(courseEdition));
        when(courseEditionConverterMock.convertFromEntity(courseEdition)).thenReturn(courseEditionDto);
        when(courseServiceMock.getCourseByCourseVersionId(any())).thenReturn(courseDto);
        when(courseEditionRepositoryMock.getOne(any())).thenReturn(courseEdition);
        List<CourseWithCourseEditionDto> courseWithCourseEditionDtoListResult = courseEditionService.getAllEditionsWithEnrolledStudent(user);

        verify(submissionServiceMock, times(1)).countAllTask(user, courseEdition);
        verify(submissionServiceMock, times(1)).countAllGradedTasks(user, courseEdition);
        verify(submissionServiceMock, times(1)).countAllSubmittedTasks(user, courseEdition);
        verify(submissionServiceMock, times(1)).countGradedLessonStatus(user, courseEdition);
        verify(submissionServiceMock, times(1)).countSubmittedLessonStatus(user, courseEdition);
        verify(lessonRepositoryMock, times(1)).getLessonsByCourseEditionId(any());
        assertThat(courseWithCourseEditionDtoListResult).size().isEqualTo(1);
        assertThat(courseWithCourseEditionDtoListResult.get(0).getCourse()).isEqualTo(courseDto.get());
        assertThat(courseWithCourseEditionDtoListResult.get(0).getCourseEdition()).isEqualTo(courseEditionDto);
    }

    @Test(expected = IllegalStateException.class)
    public void getAllEditionsWithEnrolledStudentThrowsIllegalStateExceptionTest() {
        User user = new User();
        CourseEdition courseEdition = new CourseEdition();
        CourseEditionDto courseEditionDto = new CourseEditionDto();
        CourseVersionDto courseVersionDto = new CourseVersionDto();
        courseEditionDto.setCourseVersion(courseVersionDto);

        when(courseEditionRepositoryMock.findAllCourseEditionWithEnrolledStudent(any())).thenReturn(Collections.singletonList(courseEdition));
        when(courseEditionConverterMock.convertFromEntity(courseEdition)).thenReturn(courseEditionDto);
        when(courseServiceMock.getCourseByCourseVersionId(any())).thenThrow(new IllegalStateException());
        courseEditionService.getAllEditionsWithEnrolledStudent(user);
    }
}
