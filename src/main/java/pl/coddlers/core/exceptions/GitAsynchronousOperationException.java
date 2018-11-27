package pl.coddlers.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GitAsynchronousOperationException extends RuntimeException {
    public GitAsynchronousOperationException() {
        super("Cannot perform git operation");
    }

    public GitAsynchronousOperationException(String message) {
        super(message);
    }
    
}
