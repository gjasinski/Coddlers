package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.core.models.entity.StudentLessonRepository;

public interface StudentLessonRepositoryRepository extends JpaRepository<StudentLessonRepository, Long> {
}
