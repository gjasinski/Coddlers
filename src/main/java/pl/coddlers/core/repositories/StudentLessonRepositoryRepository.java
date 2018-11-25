package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.models.entity.StudentLessonRepository;
import pl.coddlers.core.models.entity.User;

public interface StudentLessonRepositoryRepository extends JpaRepository<StudentLessonRepository, Long> {
    StudentLessonRepository findByCourseEditionAndLessonAndUser(CourseEdition courseEdition, Lesson lesson, User user);
}
