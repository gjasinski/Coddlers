package pl.coddlers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DbNotPopulatedWithDataException extends RuntimeException {
    public DbNotPopulatedWithDataException(String message) {
        super(message);
    }
}
