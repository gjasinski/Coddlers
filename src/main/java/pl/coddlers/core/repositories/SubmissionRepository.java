package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.core.models.entity.Submission;

import java.util.Collection;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
	Collection<Submission> findByTaskId(Long taskId);
}
