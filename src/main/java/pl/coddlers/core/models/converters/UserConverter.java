package pl.coddlers.core.models.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pl.coddlers.core.models.dto.UserDto;
import pl.coddlers.core.models.entity.AccountType;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.core.repositories.AccountTypeRepository;
import pl.coddlers.core.security.AccountTypeEnum;
import pl.coddlers.core.exceptions.DbNotPopulatedWithDataException;
import pl.coddlers.core.exceptions.WrongAccountTypeProvided;

import java.util.Arrays;
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
        UserDto dto = new UserDto();
        dto.setUserMail(entity.getUserMail());
        dto.setFirstname(entity.getFirstname());
        dto.setLastname(entity.getLastname());
        dto.setUserRoles(entity.getAccountTypes().stream()
                .map(accountType ->
                     AccountTypeEnum.getEnumByName(accountType.getName())
                )
                .toArray(AccountTypeEnum[]::new));

        return dto;
    }

    @Override
    public User convertFromDto(UserDto dto) {
        User userEntity = new User();

        String[] accountTypes = Arrays.stream(dto.getUserRoles())
                .map(accountTypeEnum -> {
                    String strUpperCase = accountTypeEnum.getName();
                    if (strUpperCase == null || accountTypeEnum == AccountTypeEnum.ROLE_ADMIN) {
                        throw new WrongAccountTypeProvided();
                    }

                    return strUpperCase;
                })
                .toArray(String[]::new);

        Set<AccountType> authorities = new HashSet<>();

        for (String accountTypeString : accountTypes) {
            AccountType accountType = authorityRepository.findById(accountTypeString)
                    .orElseThrow(() -> new DbNotPopulatedWithDataException("Db is not initialize with Authorities"));
            authorities.add(accountType);
        }

        userEntity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        userEntity.setUserMail(dto.getUserMail().toLowerCase());
        userEntity.setAccountTypes(authorities);
        userEntity.setFirstname(dto.getFirstname());
        userEntity.setLastname(dto.getLastname());

        return userEntity;
    }
}
