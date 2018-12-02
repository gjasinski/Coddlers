package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.CourseEditionLesson;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.repositories.CourseEditionLessonRepository;

import java.sql.Timestamp;

@Service
public class CourseEditionLessonService {
    private final CourseEditionLessonRepository courseEditionLessonRepository;

    @Autowired
    public CourseEditionLessonService(CourseEditionLessonRepository courseEditionLessonRepository) {
        this.courseEditionLessonRepository = courseEditionLessonRepository;
    }

    public CourseEditionLesson createCourseEditionLesson(CourseEdition courseEdition, Lesson lesson, Timestamp startDate) {
        CourseEditionLesson courseEditionLesson = new CourseEditionLesson();
        courseEditionLesson.setCourseEdition(courseEdition);
        courseEditionLesson.setLesson(lesson);
        Timestamp endDate = addDaysToDate(startDate, lesson.getTimeInDays());
        courseEditionLesson.setStartDate(startDate);
        courseEditionLesson.setEndDate(endDate);
        courseEditionLessonRepository.save(courseEditionLesson);

        return courseEditionLesson;
    }

    public Timestamp addDaysToDate(Timestamp date, int days) {
        return Timestamp.valueOf(date.toLocalDateTime().plusDays(days));
    }
}
