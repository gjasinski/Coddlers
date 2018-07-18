package pl.coddlers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongAccountTypeProvided extends RuntimeException {
    public WrongAccountTypeProvided() {
        super("Provided invalid AccountType value.");
    }
}
