package pl.coddlers.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class WrongDateException extends RuntimeException {

    private static final String DATE_ERROR = "Start date cannot be later than end date.";

    public WrongDateException() {
        super(DATE_ERROR);
    }
}
