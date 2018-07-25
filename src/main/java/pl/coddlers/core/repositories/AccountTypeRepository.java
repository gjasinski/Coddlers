package pl.coddlers.core.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.coddlers.core.models.entity.AccountType;

public interface AccountTypeRepository extends CrudRepository<AccountType, String> {
}
