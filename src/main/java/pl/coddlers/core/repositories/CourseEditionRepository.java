package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.core.models.entity.CourseEdition;

import java.util.Optional;

public interface CourseEditionRepository extends JpaRepository<CourseEdition, Long> {
	Optional<CourseEdition> findById(Long id);
}