package pl.coddlers.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CourseEditionNotFoundException extends RuntimeException {

	private static final String NOT_FOUND = "Could not find course edition with id: ";

	public CourseEditionNotFoundException(Long id) {
		super(NOT_FOUND + id);
	}

	public CourseEditionNotFoundException(String invitationLink) {
	    super("Course edition with invitation link " + invitationLink + " not found");
    }
}
