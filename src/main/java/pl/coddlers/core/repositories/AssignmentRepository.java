package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.core.models.entity.Assignment;

import java.util.Collection;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Optional<Assignment> findById(Long id);
    Collection<Assignment> findByCourse_Id(Long courseId);
}
