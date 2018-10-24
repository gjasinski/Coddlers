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
import pl.coddlers.core.services.LessonService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

@Slf4j
@Component
public class GitScheduledTasks {

    private LessonService lessonService;
    private CourseEditionLessonRepository courseEditionLessonRepository;
    private CourseEditionRepository courseEditionRepository;
    private LessonRepository lessonRepository;

    @Autowired
    public GitScheduledTasks(LessonService lessonService, CourseEditionLessonRepository courseEditionLessonRepository, CourseEditionRepository courseEditionRepository, LessonRepository lessonRepository) {
        this.lessonService = lessonService;
        this.courseEditionLessonRepository = courseEditionLessonRepository;
        this.courseEditionRepository = courseEditionRepository;
        this.lessonRepository = lessonRepository;
    }

    @Scheduled(cron = "0 50 23 * * 1-7", zone = "CET")
    public void lazyRepositoryForking() {
        log.info("Executed lazy forking task");
        Date dt = new Date();
        LocalDateTime localDateTime = LocalDateTime.from(dt.toInstant()).plusDays(1);
        java.sql.Date sqlDate = java.sql.Date.valueOf(localDateTime.toLocalDate());
        Collection<CourseEditionLesson> courseEditionLessons = courseEditionLessonRepository.findByDate(sqlDate);

        courseEditionLessons.forEach(courseEditionLesson -> {
            Lesson lesson = lessonRepository.getOne(courseEditionLesson.getLesson().getId());
            CourseEdition courseEdition = courseEditionRepository.getOne(courseEditionLesson.getCourseEdition().getId());
            lessonService.forkModelLesson(courseEdition, lesson).whenComplete(((studentLessonRepositories, throwable) -> {
                log.info(String.format("Created %d repositories", studentLessonRepositories.size()));
            }));
        });
    }
}
