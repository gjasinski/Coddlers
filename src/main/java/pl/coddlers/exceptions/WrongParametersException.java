package pl.coddlers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongParametersException extends RuntimeException {
    public WrongParametersException(String s) {
        super(s);
    }
}
