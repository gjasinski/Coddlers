package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.core.models.entity.Task;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
	Collection<Task> findByLessonId(Long lessonId);

	Optional<Task> findById(Long id);

	Collection<Task> findByLessonIdAndCreationTimeLessThan(long lessonId, Timestamp t1);
}
