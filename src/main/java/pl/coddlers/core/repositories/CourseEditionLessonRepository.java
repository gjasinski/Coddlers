package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.coddlers.core.models.entity.CourseEditionLesson;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface CourseEditionLessonRepository extends JpaRepository<CourseEditionLesson, Long> {
    Optional<CourseEditionLesson> findByLesson_IdAndCourseEdition_Id(long lessonId, long courseEditionId);

    List<CourseEditionLesson> findByCourseEdition_Id(long courseEditionId);

    @Query(value = "select * from course_edition_lesson as c " +
            "where date_part('YEAR', c.start_date) = date_part('YEAR', cast(?1 AS date)) and " +
            "date_part('MONTH', c.start_date) = date_part('MONTH', cast(?1 AS date)) and " +
            "date_part('DAY', c.start_date) = date_part('DAY', cast(?1 AS date))",
            nativeQuery = true)
    List<CourseEditionLesson> findByDate(Date date);
}
