package pl.coddlers.controllers;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.coddlers.models.converters.UserConverter;
import pl.coddlers.models.dto.AuthenticationDto;
import pl.coddlers.models.dto.AuthenticationResponseDto;
import pl.coddlers.models.dto.ErrorInfoDto;
import pl.coddlers.repositories.UserDataRepository;
import pl.coddlers.security.jwt.TokenUtils;
import pl.coddlers.services.UserDetailsServiceImpl;
import javax.naming.AuthenticationException;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "${jwt.route.authentication}")
public class AuthenticationController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> login(@Valid @RequestBody AuthenticationDto authenticationDto) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationDto.getMail(),
                authenticationDto.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenUtils.generateToken(authentication);

        return ResponseEntity.ok(new AuthenticationResponseDto(token));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> noSuchElementException(ConstraintViolationException ex) {
        return new ResponseEntity<>(new ErrorInfoDto(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
