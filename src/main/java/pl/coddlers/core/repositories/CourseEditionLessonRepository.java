package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.core.models.entity.CourseEditionLesson;

public interface CourseEditionLessonRepository extends JpaRepository<CourseEditionLesson, Long> {
}
