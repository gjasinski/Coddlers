package pl.coddlers.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class LessonAlreadyExists extends RuntimeException {
    public LessonAlreadyExists(String message){
        super(message);
    }
}
