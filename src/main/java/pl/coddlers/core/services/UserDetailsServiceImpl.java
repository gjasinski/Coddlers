package pl.coddlers.core.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.InternalServerErrorException;
import pl.coddlers.core.exceptions.UserNotFoundException;
import pl.coddlers.core.models.converters.UserConverter;
import pl.coddlers.core.models.dto.UserDto;
import pl.coddlers.core.models.entity.AccountType;
import pl.coddlers.core.repositories.UserDataRepository;
import pl.coddlers.git.services.GitUserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserDataRepository userDataRepository;

    private UserConverter userConverter;

    private GitUserService gitUserService;

    @Autowired
    public UserDetailsServiceImpl(UserDataRepository userDataRepository, UserConverter userConverter, GitUserService gitUserService) {
        this.userDataRepository = userDataRepository;
        this.userConverter = userConverter;
        this.gitUserService = gitUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String userMail) throws UsernameNotFoundException {
        pl.coddlers.core.models.entity.User applicationUser = userDataRepository.findFirstByUserMail(userMail);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(userMail);
        }
        return new User(applicationUser.getUserMail(), applicationUser.getPassword(),
                convertAccountTypesToSimpleGrantedAuthority(applicationUser.getAccountTypes()));
    }

    private List<SimpleGrantedAuthority> convertAccountTypesToSimpleGrantedAuthority(Set<AccountType> accountTypes) {
        return accountTypes.stream()
                .map((auth)-> new SimpleGrantedAuthority(auth.getName()))
                .collect(Collectors.toList());
    }

    public pl.coddlers.core.models.entity.User getCurrentUserEntity() {
        // TODO could be moved to SecurityUtils tools class
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Optional<User> user = Optional.ofNullable(securityContext.getAuthentication())
                .filter(auth -> auth.getPrincipal() instanceof User)
                .map(auth -> (User) auth.getPrincipal());

        if (user.isPresent()) {
            return userDataRepository.findFirstByUserMail(user.get().getUsername());
        } else {
            throw new UserNotFoundException();
        }
    }

    public UserDto getUserDtoWithAccountTypes() {
        return userConverter.convertFromEntity(getCurrentUserEntity());
    }

    public pl.coddlers.core.models.entity.User registerUser(UserDto userDto) {
        pl.coddlers.core.models.entity.User userEntity = userConverter.convertFromDto(userDto);

        Long gitUserId = 0l;
        try {
            gitUserId = gitUserService.createUser(userEntity.getUserMail(), userEntity.getFullName(), makeUserName(userEntity),
                    userDto.getPassword()).get();
            log.info("Created gitlab account with id "+gitUserId.toString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("Couldn't register a user. Please contact with administrator.");
        }
        userEntity.setGitUserId(gitUserId);
        userDataRepository.save(userEntity);
        log.debug("Created Information for User: {}", userEntity);

        return userEntity;
    }

    private String makeUserName(pl.coddlers.core.models.entity.User userEntity) {
        return userEntity.getFirstname().substring(0, 1) + userEntity.getLastname();
    }
}