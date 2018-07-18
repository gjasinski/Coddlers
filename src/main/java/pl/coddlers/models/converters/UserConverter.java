package pl.coddlers.models.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pl.coddlers.exceptions.DbNotPopulatedWithDataException;
import pl.coddlers.exceptions.WrongAccountTypeProvided;
import pl.coddlers.models.dto.UserDto;
import pl.coddlers.models.entity.AccountType;
import pl.coddlers.models.entity.User;
import pl.coddlers.repositories.AccountTypeRepository;
import pl.coddlers.security.AccountTypeConstants;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserConverter implements BaseConverter<User, UserDto> {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AccountTypeRepository authorityRepository;

    @Override
    public UserDto convertFromEntity(User entity) {
        return null;
    }

    @Override
    public User convertFromDto(UserDto dto) {
        User userEntity = new User();

        String accountTypeString = dto.getUserRole().toUpperCase();
        if (!accountTypeString.equals(AccountTypeConstants.STUDENT) &&
                !accountTypeString.equals(AccountTypeConstants.TEACHER)) {
            throw new WrongAccountTypeProvided();
        }

        AccountType accountType = authorityRepository.findById(accountTypeString)
                .orElseThrow(() -> new DbNotPopulatedWithDataException("Db is not initialize with Authorities"));
        Set<AccountType> authorities = new HashSet<>();
        authorities.add(accountType);

        userEntity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        userEntity.setUserMail(dto.getUserMail().toLowerCase());
        userEntity.setAuthorities(authorities);
        userEntity.setFirstname(dto.getFirstname());
        userEntity.setLastname(dto.getLastname());

        return userEntity;
    }
}
