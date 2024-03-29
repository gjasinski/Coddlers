package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.coddlers.core.models.entity.CourseEdition;

import java.util.List;
import java.util.Optional;

public interface CourseEditionRepository extends JpaRepository<CourseEdition, Long> {
    Optional<CourseEdition> findById(Long id);

    List<CourseEdition> findAllByCourseVersionId(Long courseVersionId);

    Optional<CourseEdition> findByInvitationToken(String invitationToken);

    @Query("select e from CourseEdition e " +
            "join e.users u " +
            "where u.id = :studentId")
    List<CourseEdition> findAllCourseEditionWithEnrolledStudent(@Param("studentId") Long studentId);
}
