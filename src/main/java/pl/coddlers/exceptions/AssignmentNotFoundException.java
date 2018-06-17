package pl.coddlers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AssignmentNotFoundException extends RuntimeException {
    public AssignmentNotFoundException(Long id) {
        super("Could not find assignment with id: "+id+".");
    }
}
