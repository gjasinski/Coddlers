package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.core.models.entity.Edition;

import java.util.Optional;

public interface EditionRepository extends JpaRepository<Edition, Long> {
	Optional<Edition> findById(Long id);
}
