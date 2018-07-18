package pl.coddlers.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.coddlers.models.entity.AccountType;

public interface AccountTypeRepository extends CrudRepository<AccountType, String> {
}
