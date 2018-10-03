package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.CourseEditionNotFoundException;
import pl.coddlers.core.models.converters.CourseEditionConverter;
import pl.coddlers.core.models.dto.CourseEditionDto;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.CourseEditionLesson;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.models.entity.StudentLessonRepository;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.core.repositories.CourseEditionLessonRepository;
import pl.coddlers.core.repositories.CourseEditionRepository;
import pl.coddlers.core.repositories.LessonRepository;
import pl.coddlers.core.repositories.StudentLessonRepositoryRepository;
import pl.coddlers.git.services.GitLessonService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CourseEditionService {
    private final StudentLessonRepositoryRepository studentLessonRepositoryRepository;
    private final CourseEditionRepository courseEditionRepository;
    private final CourseEditionConverter courseEditionConverter;
    private final LessonRepository lessonRepository;
    private final CourseEditionLessonRepository courseEditionLessonRepository;
    private final GitLessonService gitLessonService;

    @Autowired
    public CourseEditionService(StudentLessonRepositoryRepository studentLessonRepositoryRepository, CourseEditionRepository courseEditionRepository, CourseEditionConverter courseEditionConverter, LessonRepository lessonRepository, CourseEditionLessonRepository courseEditionLessonRepository, GitLessonService gitLessonService) {
        this.studentLessonRepositoryRepository = studentLessonRepositoryRepository;
        this.courseEditionRepository = courseEditionRepository;
        this.courseEditionConverter = courseEditionConverter;
        this.lessonRepository = lessonRepository;
        this.courseEditionLessonRepository = courseEditionLessonRepository;
        this.gitLessonService = gitLessonService;
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

    public CompletableFuture<List<StudentLessonRepository>> forkModelRepositoriesForStudent(CourseEdition courseEdition, User user) {
        // TODO: 30.09.18 error handling
        List<CompletableFuture<StudentLessonRepository>> listOfFutureRepositories = cloneModelRepositories(courseEdition, user);
        return mapListOfFuturesToFutureOfList(listOfFutureRepositories).thenApply(studentLessonRepositoryRepository::saveAll);
    }

    private List<CompletableFuture<StudentLessonRepository>> cloneModelRepositories(CourseEdition courseEdition, User user) {
        return courseEdition.getCourseVersion()
                .getLessons()
                .stream()
                .map(lesson -> gitLessonService.forkLesson(lesson, user, courseEdition))
                .collect(Collectors.toList());
    }

    private CompletableFuture<List<StudentLessonRepository>> mapListOfFuturesToFutureOfList(List<CompletableFuture<StudentLessonRepository>> listOfFutureRepositories) {
        return CompletableFuture.allOf(listOfFutureRepositories.toArray(new CompletableFuture[listOfFutureRepositories.size()]))
                .thenApply(v ->
                        listOfFutureRepositories.stream()
                                .map(CompletableFuture::join)
                                .collect(Collectors.toList())
                );
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
