package pl.coddlers.git.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.coddlers.git.Exceptions.GitErrorHandler;
import pl.coddlers.git.models.ResponseWithIdDTO;

@Service
public class GitUserService {
	private static final String PRIVATE_TOKEN = "private_token";
	private static final String SKIP_CONFIRMATION = "skip_confirmation";
	private static final String NAME = "name";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String EMAIL = "email";
	private RestTemplate restTemplate;

	@Value("${gitlab.api.host}:${gitlab.api.http.port}${gitlab.api.prefix}")
	private String gitlabApi;

	@Value("${gitlab.api.apiuser.private_token}")
	private String privateToken;

	public GitUserService() {
		this.restTemplate = new RestTemplate();
		this.restTemplate.setErrorHandler(new GitErrorHandler());
	}

	public Long createUser(String email, String name, String username, String password) {
		String resourceUrl = gitlabApi + "/users";

		HttpHeaders headers = getHttpHeaders();
		UriComponentsBuilder builder = createComponentBuilder(resourceUrl)
				.queryParam(EMAIL, email)
				.queryParam(PASSWORD, password)
				.queryParam(USERNAME, username)
				.queryParam(NAME, name)
				.queryParam(SKIP_CONFIRMATION, Boolean.TRUE);


		HttpEntity<?> entity = new HttpEntity<>(headers);

		return restTemplate.exchange(
				builder.build().toUriString(),
				HttpMethod.POST,
				entity,
				ResponseWithIdDTO.class)
				.getBody()
				.getId();
	}

	private HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

	private UriComponentsBuilder createComponentBuilder(String resourceUrl) {
		return UriComponentsBuilder.fromHttpUrl(resourceUrl)
				.queryParam(PRIVATE_TOKEN, privateToken);
	}
}
