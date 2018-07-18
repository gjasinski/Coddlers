package pl.coddlers.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AssignmentNotFoundException extends RuntimeException {

	private static final String NOT_FOUND = "Could not find assignment with id: ";

	public AssignmentNotFoundException(Long id) {
		super(NOT_FOUND + id);
	}
}
