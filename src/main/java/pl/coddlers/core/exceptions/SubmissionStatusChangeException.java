package pl.coddlers.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SubmissionStatusChangeException extends RuntimeException {
    public SubmissionStatusChangeException(String message) {
        super(message);
    }
}
