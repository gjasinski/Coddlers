package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.coddlers.exceptions.EmailAlreadyUsedException;
import pl.coddlers.core.models.dto.UserDto;
import pl.coddlers.core.repositories.UserDataRepository;
import pl.coddlers.core.services.UserDetailsServiceImpl;
import javax.validation.Valid;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody UserDto userDto) throws URISyntaxException {
        userDataRepository.findByUserMail(userDto.getUserMail().toLowerCase()).ifPresent(u -> {throw new EmailAlreadyUsedException();});

        userDetailsService.registerUser(userDto);
        // TODO send activation email
    }

    @GetMapping
    public UserDto getAccount() {
        return userDetailsService.getUserDtoWithAccountTypes();
    }

}
