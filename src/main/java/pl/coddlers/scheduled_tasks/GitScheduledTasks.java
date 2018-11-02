package pl.coddlers.scheduled_tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.CourseEditionLesson;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.repositories.CourseEditionLessonRepository;
import pl.coddlers.core.repositories.CourseEditionRepository;
import pl.coddlers.core.repositories.LessonRepository;
import pl.coddlers.core.repositories.StudentLessonRepositoryRepository;
import pl.coddlers.core.services.LessonService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Slf4j
@Component
public class GitScheduledTasks {

    private LessonService lessonService;
    private CourseEditionLessonRepository courseEditionLessonRepository;
    private CourseEditionRepository courseEditionRepository;
    private LessonRepository lessonRepository;
    private StudentLessonRepositoryRepository studentLessonRepositoryRepository;

    @Autowired
    public GitScheduledTasks(LessonService lessonService, CourseEditionLessonRepository courseEditionLessonRepository,
                             CourseEditionRepository courseEditionRepository, LessonRepository lessonRepository, StudentLessonRepositoryRepository studentLessonRepositoryRepository) {
        this.lessonService = lessonService;
        this.courseEditionLessonRepository = courseEditionLessonRepository;
        this.courseEditionRepository = courseEditionRepository;
        this.lessonRepository = lessonRepository;
        this.studentLessonRepositoryRepository = studentLessonRepositoryRepository;
    }

    @Scheduled(cron = "0 50 23 * * 1-7", zone = "CET")
    public void lazyRepositoryForking() {
        log.info("Executed lazy forking task");
        LocalDate localDate = LocalDate.now().plusDays(1);
        Timestamp t1 = Timestamp.valueOf(localDate.atTime(0, 0));
        Timestamp t2 = Timestamp.valueOf(localDate.atTime(23, 59, 59));

        Collection<CourseEditionLesson> courseEditionLessons = courseEditionLessonRepository.findAllByStartDateBetween(t1, t2);
        log.debug(String.format("Forking repositories for all enrolled students for this courseEditionLessons: %s",
                courseEditionLessons.toString()));

        courseEditionLessons.forEach(courseEditionLesson -> {
            try {
                Lesson lesson = lessonRepository.findById(courseEditionLesson.getLesson().getId()).get();
                CourseEdition courseEdition = courseEditionRepository.findById(courseEditionLesson.getCourseEdition().getId()).get();
                lessonService.forkModelLesson(courseEdition, lesson)
                        .whenComplete(((studentLessonRepositories, throwable) -> {
                            studentLessonRepositoryRepository.saveAll(studentLessonRepositories);
                            log.info(String.format("Created %d repositories", studentLessonRepositories.size()));
                        }))
                        .exceptionally(ex -> {
                            log.error(String.format("Error while forking lesson with id %d for courseEdition with id %d", lesson.getId(), courseEdition.getId()), ex);
                            return Collections.emptyList();
                        });
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        });
    }
}
