package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.core.models.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
