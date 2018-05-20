package pl.coddlers.git.projects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GitProjectService {
	private RestTemplate restTemplate = new RestTemplate();

	@Value("${gitlab.api.host}:${gitlab.api.http.port}${gitlab.api.prefix}")
	private String gitlabApi;

	@Value("${gitlab.api.apiuser.private_token}")
	private String private_token;

	HttpEntity<String> createUser(long userId, String name) {
		String resourceUrl = gitlabApi + "/projects/user/" + userId;

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(resourceUrl)
				.queryParam("private_token", private_token)
				.queryParam("name", name)
				.queryParam("visibility", "private");


		HttpEntity<?> entity = new HttpEntity<>(headers);

		ResponseEntity<String> exchange = restTemplate.exchange(
				builder.build().toUriString(),
				HttpMethod.POST,
				entity,
				String.class);
		long id = extractId(exchange);

		createBranch(id, "task1-develop");
		createBranch(id, "task1-master");
		createBranch(id, "task2-develop");
		createBranch(id, "task2-master");
		return exchange;
	}

	private HttpEntity<String> createBranch(long repositoryId, String branch) {
		String resourceUrl = gitlabApi + " /projects/" + repositoryId + "/repository/branches";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(resourceUrl)
				.queryParam("private_token", private_token)
				.queryParam("repositoryId", repositoryId)
				.queryParam("branch", branch)
				.queryParam("ref", "master");



		HttpEntity<?> entity = new HttpEntity<>(headers);

		return restTemplate.exchange(
				builder.build().toUriString(),
				HttpMethod.POST,
				entity,
				String.class);
	}

	private long extractId(ResponseEntity<String> exchange) {
		String body = exchange.getBody();
		System.out.println(body);
		String id = body.substring(body.indexOf("id") + 4, body.indexOf(","));
		return Long.valueOf(id);
	}


}
