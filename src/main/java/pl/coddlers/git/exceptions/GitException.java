package pl.coddlers.git.exceptions;

import java.io.IOException;

public class GitException extends IOException {

	GitException(String message) {
		super(message);
	}
}
