package pl.coddlers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.coddlers.models.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> getById(Long id);

    @Query("SELECT c FROM Course c WHERE c.id >= :startsAt")
    List<Course> getCoursesWithIdGreaterEqThan(@Param("startsAt") Long startsAt);

    @Query("SELECT c FROM Course c WHERE c.id >= :startsAt and c.id <= :endAt")
    List<Course> getCoursesWithIdFromInclusiveRange(@Param("startsAt") Long startsAt, @Param("endAt") Long endAt);

}
