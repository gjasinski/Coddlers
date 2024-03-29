package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.core.models.entity.CourseEditionLesson;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface CourseEditionLessonRepository extends JpaRepository<CourseEditionLesson, Long> {
    Optional<CourseEditionLesson> findByLesson_IdAndCourseEdition_Id(long lessonId, long courseEditionId);

    List<CourseEditionLesson> findByCourseEdition_Id(long courseEditionId);

    List<CourseEditionLesson> findAllByStartDateBetween(Timestamp t1, Timestamp t2);

    Optional<CourseEditionLesson> findFirstByCourseEdition_IdOrderByStartDateDesc(long courseEditionId);
}
