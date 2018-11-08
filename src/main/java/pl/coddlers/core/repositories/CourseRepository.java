package pl.coddlers.core.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.coddlers.core.models.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
	Optional<Course> getById(Long id);

	@Query("select c from Course c")
	List<Course> getPaginatedCourses(Pageable pageable);

	@Query("select c from Course c " +
			"join c.teachers t " +
			"where t.user.id = :userId")
	List<Course> getByUserId(@Param("userId") Long userId);

	Optional<Course> findByCourseVersion_Id(Long courseVersionId);

	@Query("select c from Course c " +
	"join c.courseVersion v " +
	"join v.courseEditions e "+
	"where e.id = :courseEditionId ")
	Optional<Course> getByCourseEditionId(@Param("courseEditionId") Long courseEditionId);
}
