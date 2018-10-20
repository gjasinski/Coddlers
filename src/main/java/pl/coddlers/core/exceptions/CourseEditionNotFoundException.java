package pl.coddlers.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CourseEditionNotFoundException extends RuntimeException {

	private static final String NOT_FOUND_BY_ID = "Could not find course edition with id: ";
	private static final String NOT_FOUND_BY_INVITATION_TOKEN = "Could not find course edition with invitation token: ";

	public CourseEditionNotFoundException(Long id) {
		super(NOT_FOUND_BY_ID + id);
	}

	public CourseEditionNotFoundException(String invitationToken) {
	    super(NOT_FOUND_BY_INVITATION_TOKEN + invitationToken);
    }
}
