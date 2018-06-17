package pl.coddlers.git.Exceptions;

import java.io.IOException;

public class GitException extends IOException {

	GitException(String message) {
		super(message);
	}
}
