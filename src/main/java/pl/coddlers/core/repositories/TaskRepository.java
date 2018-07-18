package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.core.models.entity.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Collection<Task> findByAssignment_Id(Long assignmentId);

    Optional<Task> findById(Long id);
}
