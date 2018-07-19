package pl.coddlers.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends RuntimeException {

    private static final String NOT_FOUND = "Could not find task with id: ";

    public TaskNotFoundException(Long id) {
        super(NOT_FOUND + id);
    }
}
