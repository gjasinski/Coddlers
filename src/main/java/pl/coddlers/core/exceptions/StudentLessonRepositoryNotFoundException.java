package pl.coddlers.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StudentLessonRepositoryNotFoundException extends RuntimeException {

    private static final String NOT_FOUND = "Could not find student lesson repository with id: ";

    public StudentLessonRepositoryNotFoundException(Long id) {
        super(NOT_FOUND + id);
    }
}
