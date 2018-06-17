package pl.coddlers.git.services;

import org.springframework.beans.factory.annotation.Autowired;
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
import pl.coddlers.git.models.Hook;
import pl.coddlers.git.models.ResponseWithIdDTO;
import pl.coddlers.git.reposiories.HookRepository;

import java.util.List;

@Service
public class GitProjectService {
	//todo this should be injected from properties
	private static final String GIT_HOOKS_ENDPOINT = "http://coddlers.pl:8080/api/git/hooks";
	private RestTemplate restTemplate;

	@Value("${gitlab.api.host}:${gitlab.api.http.port}${gitlab.api.prefix}")
	private String gitlabApi;

	@Value("${gitlab.api.apiuser.private_token}")
	private String private_token;

	private final HookRepository hookRepository;

	@Autowired
	public GitProjectService(HookRepository hookRepository) {
		this.restTemplate = new RestTemplate();
		this.restTemplate.setErrorHandler(new GitErrorHandler());
		this.hookRepository = hookRepository;
	}

	public Long createCourse(long tutorGitId, String courseName) {
		String resourceUrl = gitlabApi + "/projects/user/" + tutorGitId;

		HttpHeaders headers = getHttpHeaders();

		UriComponentsBuilder builder = createComponentBuilder(resourceUrl)
				.queryParam("name", courseName)
				.queryParam("visibility", "private");

		HttpEntity<?> entity = new HttpEntity<>(headers);

		Long projectId = restTemplate.exchange(
				builder.build().toUriString(),
				HttpMethod.POST,
				entity,
				ResponseWithIdDTO.class)
				.getBody()
				.getId();
		createGitHook(projectId);
		return projectId;
	}

	public void addStudentToCourse(Long courseGitId, Long studentId) {
		String resourceUrl = gitlabApi + "/projects/" + courseGitId + "/fork";
		HttpHeaders headers = getHttpHeaders();

		UriComponentsBuilder builder = createComponentBuilder(resourceUrl)
				.queryParam("namespace", studentId);


		HttpEntity<?> entity = new HttpEntity<>(headers);

		ResponseEntity<ResponseWithIdDTO> exchange = restTemplate.exchange(
				builder.build().toUriString(),
				HttpMethod.POST,
				entity,
				ResponseWithIdDTO.class);
		Long studentCouseId = exchange.getBody().getId();
		registerHooks(courseGitId, studentCouseId);
	}

	private void createGitHook(Long projectId) {
		String resourceUrl = gitlabApi + "/projects/" + projectId + "/hooks";
		HttpHeaders headers = getHttpHeaders();

		UriComponentsBuilder builder = createComponentBuilder(resourceUrl)
				.queryParam("url", GIT_HOOKS_ENDPOINT)
				.queryParam("push_events", "true");


		HttpEntity<?> entity = new HttpEntity<>(headers);

		restTemplate.exchange(
				builder.build().toUriString(),
				HttpMethod.POST,
				entity,
				String.class);
	}

	private HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

	private UriComponentsBuilder createComponentBuilder(String resourceUrl) {
		return UriComponentsBuilder.fromHttpUrl(resourceUrl)
				.queryParam("private_token", private_token);
	}

	private void registerHooks(Long originProjectGitId, Long createdProjectId) {
		List<Hook> allByProjectId = hookRepository.getAllByProjectId(originProjectGitId);
		allByProjectId.stream()
				.map(hook -> createHook(createdProjectId, hook))
				.forEach(hookRepository::save);
	}

	private Hook createHook(Long createdProjectId, Hook hook) {
		Hook createdHook = new Hook();
		createdHook.setProjectId(createdProjectId);
		createdHook.setBranch(hook.getBranch());
		return hook;
	}
}
