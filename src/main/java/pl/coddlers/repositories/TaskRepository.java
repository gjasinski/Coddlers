package pl.coddlers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.models.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignmentId(Long id);

    Optional<Task> findById(Long id);
}
