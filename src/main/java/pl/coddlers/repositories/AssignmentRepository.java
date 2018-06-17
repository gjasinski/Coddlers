package pl.coddlers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.models.entity.Assignment;

import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Optional<Assignment> findById(Long id);
}
