package pl.coddlers.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CourseVersionNotFound extends RuntimeException {
    public CourseVersionNotFound(Long courseId, Integer courseVersionNumber) {
        super(String.format("Cannot find version %d of course with id %d", courseVersionNumber, courseId));
    }

    public CourseVersionNotFound(Long courseVersionId) {
        super(String.format("Cannot find course version with id %d", courseVersionId));
    }
}
