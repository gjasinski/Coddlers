package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.core.models.entity.CourseVersion;

public interface CourseVersionRepository extends JpaRepository<CourseVersion, Long> {
}
