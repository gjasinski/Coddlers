package pl.coddlers.core.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.coddlers.core.models.converters.UserConverter;
import pl.coddlers.core.models.dto.UserDto;
import pl.coddlers.core.repositories.UserDataRepository;

import java.util.stream.Collectors;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private UserConverter userConverter;

    public UserDetailsServiceImpl(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userMail) throws UsernameNotFoundException {
        pl.coddlers.core.models.entity.User applicationUser = userDataRepository.findFirstByUserMail(userMail);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(userMail);
        }
        return new User(applicationUser.getUserMail(), applicationUser.getPassword(),
                applicationUser.getAuthorities().stream()
                .map((auth)-> new SimpleGrantedAuthority(auth.getName()))
                        .collect(Collectors.toList()));
    }

    public pl.coddlers.core.models.entity.User getCurrentUserEntity() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDataRepository.findFirstByUserMail(user.getUsername());
    }

    public pl.coddlers.core.models.entity.User registerUser(UserDto userDto) {
        pl.coddlers.core.models.entity.User userEntity = userConverter.convertFromDto(userDto);

        userDataRepository.save(userEntity);
        log.debug("Created Information for User: {}", userEntity);

        return userEntity;
    }
}