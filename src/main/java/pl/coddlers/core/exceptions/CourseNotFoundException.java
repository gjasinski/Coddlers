package pl.coddlers.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CourseNotFoundException extends RuntimeException {

	private static final String NOT_FOUND = "Could not find course with id: ";

	public CourseNotFoundException(Long id) {
		super(NOT_FOUND + id);
	}

	public CourseNotFoundException() {
		super("");
	}
}
