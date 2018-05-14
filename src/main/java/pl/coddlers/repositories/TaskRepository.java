package pl.coddlers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.models.entity.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignment_id(Long id);
}
