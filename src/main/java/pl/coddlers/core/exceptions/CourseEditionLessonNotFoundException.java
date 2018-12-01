package pl.coddlers.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CourseEditionLessonNotFoundException extends RuntimeException {
    public CourseEditionLessonNotFoundException(Long lessonId, Long editionId) {
        super(String.format("Could not find course edition lesson with lessonId %d and editionId %d", lessonId, editionId));
    }

    public CourseEditionLessonNotFoundException(long courseEditionLessonId) {
        super(String.format("Could not find course edition lesson with id %d", courseEditionLessonId));
    }
}
