package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.coddlers.core.models.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDataRepository extends CrudRepository<User, Long> {
    User findFirstByUserMail(String userMail);

    Optional<User> findByUserMail(String userMail);

    @Query("select u from User u " +
            "join u.courseEditions e " +
            "where e.id = :courseEditionId")
    List<User> getCourseEditionUsers(@Param("courseEditionId") Long courseEditionId);
}
