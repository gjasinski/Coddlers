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
public class GitLessonService {
	private static final String PROJECTS = "/projects/";
	private static final String NAME = "name";
	private static final String VISIBILITY = "visibility";
	private static final String PRIVATE = "private";
	private static final String FORK = "/fork";
	private static final String HOOKS = "/hooks";
	private static final String NAMESPACE = "namespace";
	private static final String URL = "url";
	private static final String PUSH_EVENTS = "push_events";
	private static final String PRIVATE_TOKEN = "private_token";
	private static final String USER = "user/";

	@Value("${pl.coddlers.git.host}:${pl.coddlers.git.port}${pl.coddlers.git.event.url}")
	private String gitEventEndpoint;

	@Value("${gitlab.api.host}:${gitlab.api.http.port}${gitlab.api.prefix}" + PROJECTS)
	private String gitlabApiProjects;

	@Value("${gitlab.api.apiuser.private_token}")
	private String privateToken;

	private final HookRepository hookRepository;

	private RestTemplate restTemplate;

	@Autowired
	public GitLessonService(HookRepository hookRepository) {
		this.restTemplate = new RestTemplate();
		this.restTemplate.setErrorHandler(new GitErrorHandler());
		this.hookRepository = hookRepository;
	}

	public Long createLesson(long tutorGitId, String lessonName) {
		String resourceUrl = gitlabApiProjects + USER + tutorGitId;

		HttpHeaders headers = getHttpHeaders();

		UriComponentsBuilder builder = createComponentBuilder(resourceUrl)
				.queryParam(NAME, lessonName)
				.queryParam(VISIBILITY, PRIVATE);

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

	public Long forkLesson(Long lessonId, Long userId) {
		String resourceUrl = gitlabApiProjects + lessonId + FORK;
		HttpHeaders headers = getHttpHeaders();

		UriComponentsBuilder builder = createComponentBuilder(resourceUrl)
				.queryParam(NAMESPACE, userId);


		HttpEntity<?> entity = new HttpEntity<>(headers);

		ResponseEntity<ResponseWithIdDTO> exchange = restTemplate.exchange(
				builder.build().toUriString(),
				HttpMethod.POST,
				entity,
				ResponseWithIdDTO.class);
		Long studentCourseId = exchange.getBody().getId();
		registerHooks(lessonId, studentCourseId);
		return studentCourseId;
	}

	private void createGitHook(Long projectId) {
		String resourceUrl = gitlabApiProjects + projectId + HOOKS;
		HttpHeaders headers = getHttpHeaders();

		UriComponentsBuilder builder = createComponentBuilder(resourceUrl)
				.queryParam(URL, gitEventEndpoint)
				.queryParam(PUSH_EVENTS, Boolean.TRUE);


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
				.queryParam(PRIVATE_TOKEN, privateToken);
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
		return createdHook;
	}
}
