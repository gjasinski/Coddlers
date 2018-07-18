package pl.coddlers.core.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.coddlers.core.models.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
	Optional<Course> getById(Long id);

	// TODO fix
	@Query("select c from Course c")
	List<Course> getPaginatedCourses(Pageable pageable);
}
