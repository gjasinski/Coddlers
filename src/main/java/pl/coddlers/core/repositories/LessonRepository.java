package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.core.models.entity.Lesson;

import java.util.Collection;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
	Optional<Lesson> findById(Long id);

	Collection<Lesson> findByCourse_Id(Long courseId);
}
