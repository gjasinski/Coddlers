package pl.coddlers.git.exceptions;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class GitErrorHandler implements ResponseErrorHandler {
	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		throw new GitException(response.getRawStatusCode() + " " + response.getStatusCode().getReasonPhrase());
	}

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return !response.getStatusCode().is2xxSuccessful();
	}
}
