package pl.coddlers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.models.entity.Assignment;
import pl.coddlers.models.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> getById(Long id);
}
