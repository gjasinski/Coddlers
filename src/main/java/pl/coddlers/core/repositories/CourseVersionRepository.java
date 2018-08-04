package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.coddlers.core.models.entity.CourseVersion;

import java.util.List;
import java.util.Optional;

public interface CourseVersionRepository extends JpaRepository<CourseVersion, Long> {
    Optional<CourseVersion> findByCourse_IdAndVersionNumber(Long courseId, Integer versionNumber);
    List<CourseVersion> findByCourse_IdOrderByVersionNumberDesc(Long courseId);
}
