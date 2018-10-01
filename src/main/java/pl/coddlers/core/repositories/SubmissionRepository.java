package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.core.models.entity.Submission;

import java.util.Collection;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
	Collection<Submission> findByTaskId(Long taskId);
	Optional<Submission> findByBranchNameAndStudentLessonRepository_RepositoryUrl(String branchName, String repositoryUtl);
}
