package pl.coddlers.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.coddlers.models.entity.User;
import java.util.Optional;

public interface UserDataRepository extends CrudRepository<User, Long> {
    User findFirstByUserMail(String userMail);
    Optional<User> findByUserMail(String userMail);
}
