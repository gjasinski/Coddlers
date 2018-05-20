package pl.coddlers.git.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
class GitUserService {
	private RestTemplate restTemplate = new RestTemplate();

	@Value("${gitlab.api.host}:${gitlab.api.http.port}${gitlab.api.prefix}")
	private String gitlabApi;

	@Value("${gitlab.api.apiuser.private_token}")
	private String private_token;


	HttpEntity<String> createUser(String email, String name, String username, String password) {
		String fooResourceUrl = gitlabApi + "/users";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(fooResourceUrl)
				.queryParam("private_token", private_token)
				.queryParam("email", email)
				.queryParam("password", password)
				.queryParam("username", username)
				.queryParam("name", name)
				.queryParam("skip_confirmation", "true");


		HttpEntity<?> entity = new HttpEntity<>(headers);

		return restTemplate.exchange(
				builder.build().toUriString(),
				HttpMethod.POST,
				entity,
				String.class);
	}

}
