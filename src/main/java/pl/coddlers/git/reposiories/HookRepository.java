package pl.coddlers.git.reposiories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.git.models.Hook;

import java.util.List;
import java.util.Optional;

public interface HookRepository extends JpaRepository<Hook, Long> {
	Optional<Hook> getByProjectIdAndBranch(Long projectId, String branch);

	List<Hook> getAllByProjectId(Long projectId);
}
