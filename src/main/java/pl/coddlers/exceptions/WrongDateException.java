package pl.coddlers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class WrongDateException extends RuntimeException {
    public WrongDateException() {
        super("Start date cannot be later than end date.");
    }
}
