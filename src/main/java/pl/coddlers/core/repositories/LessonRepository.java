package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.coddlers.core.models.entity.Lesson;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Optional<Lesson> findById(Long id);

    List<Lesson> findByCourseVersionId(Long courseVersionId);

    @Query("select l from Lesson l " +
            "join l.courseVersion v " +
            "where v.id = :courseVersionId")
    List<Lesson> getCourseEditionLessons(@Param("courseVersionId") Long courseVersionId);

    @Query("select l from Lesson l " +
            "join l.courseVersion v " +
            "join v.courseEditions e " +
            "where e.id = :courseEditionId")
    List<Lesson> getLessonsByCourseEditionId(@Param("courseEditionId") Long courseEditionId);

    @Query("select l from Lesson l " +
            "join l.tasks t " +
            "where t.id = :taskId")
    Optional<Lesson> findByTaskId(@Param("taskId") Long taskId);
}
