package pl.coddlers.git.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.coddlers.git.Exceptions.GitErrorHandler;
import pl.coddlers.git.models.ResponseWithIdDTO;

@Service
class GitUserService {
	private RestTemplate restTemplate;

	@Value("${gitlab.api.host}:${gitlab.api.http.port}${gitlab.api.prefix}")
	private String gitlabApi;

	@Value("${gitlab.api.apiuser.private_token}")
	private String private_token;

	public GitUserService() {
		this.restTemplate = new RestTemplate();
		this.restTemplate.setErrorHandler(new GitErrorHandler());
	}

	public Long createUser(String email, String name, String username, String password) {
		String resourceUrl = gitlabApi + "/users";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(resourceUrl)
				.queryParam("private_token", private_token)
				.queryParam("email", email)
				.queryParam("password", password)
				.queryParam("username", username)
				.queryParam("name", name)
				.queryParam("skip_confirmation", "true");


		HttpEntity<?> entity = new HttpEntity<>(headers);

		ResponseEntity<ResponseWithIdDTO> exchange = restTemplate.exchange(
				builder.build().toUriString(),
				HttpMethod.POST,
				entity,
				ResponseWithIdDTO.class);
		return exchange.getBody().getId();
	}

}
