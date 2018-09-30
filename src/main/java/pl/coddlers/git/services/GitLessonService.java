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
import pl.coddlers.git.models.event.ProjectDto;
import pl.coddlers.git.reposiories.HookRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

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
	private static final String ID = "id";
	private static final String DATE_FORMAT = "ddMMyyyyhhmmss";

	@Value("${pl.coddlers.git.host}:${pl.coddlers.git.port}${pl.coddlers.git.event.url}")
	private String gitEventEndpoint;

    @Value("${gitlab.api.host}:${gitlab.api.http.port}${gitlab.api.prefix}" + "/" + PROJECTS + "/")
	private String gitlabApiProjects;

	@Value("${gitlab.api.apiuser.private_token}")
	private String privateToken;

	private final HookRepository hookRepository;

    private RestTemplate restTemplate;

    private ExecutorService executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());

    @Autowired
    public GitLessonService(HookRepository hookRepository) {
        this.restTemplate = new RestTemplate();
        this.restTemplate.setErrorHandler(new GitErrorHandler());
        this.hookRepository = hookRepository;
    }

    public CompletableFuture<ProjectDto> createLesson(long tutorGitId, String lessonName) {
        return CompletableFuture.supplyAsync(createLessonSupplier(tutorGitId, lessonName), executor);
    }

    private Supplier<ProjectDto> createLessonSupplier(long tutorGitId, String lessonName) {
        return () -> {
            String resourceUrl = gitlabApiProjects + USER + tutorGitId;
	        UriComponentsBuilder builder = createComponentBuilder(resourceUrl)
                    .queryParam(NAME, lessonName)
                    .queryParam(VISIBILITY, PRIVATE);
	        HttpEntity<?> entity = getHttpEntity();

	        ProjectDto body = restTemplate.exchange(
			        builder.build().toUriString(),
			        HttpMethod.POST,
			        entity,
			        ProjectDto.class)
			        .getBody();
	        createGitHook(body.getId());
            return body;
        };
    }

	public CompletableFuture<ProjectDto> forkLesson(Long lessonId, Long userId, Long courseEditionId) {
        return CompletableFuture.supplyAsync(forkLessonSupplier(lessonId, userId), executor)
		        .thenApply(projectDto -> renameForkedRepository(courseEditionId, projectDto));
    }

    private Supplier<ProjectDto> forkLessonSupplier(Long lessonId, Long userId) {
        return () -> {
            String resourceUrl = gitlabApiProjects + lessonId + FORK;
            UriComponentsBuilder builder = createComponentBuilder(resourceUrl)
                    .queryParam(NAMESPACE, userId);
	        HttpEntity<?> entity = getHttpEntity();

            ResponseEntity<ProjectDto> exchange = restTemplate.exchange(
                    builder.build().toUriString(),
                    HttpMethod.POST,
                    entity,
		            ProjectDto.class);
			return exchange.getBody();
        };
    }

	private ProjectDto renameForkedRepository(Long courseEditionId, ProjectDto projectDto) {
		String resourceUrl = gitlabApiProjects + projectDto.getId();
		UriComponentsBuilder builder = createComponentBuilder(resourceUrl)
				.queryParam(ID, projectDto.getId())
				.queryParam(NAME, buildForkedRepositoryName(courseEditionId, projectDto));
		HttpEntity<?> entity = getHttpEntity();

		ResponseEntity<ProjectDto> exchange = restTemplate.exchange(
				builder.build().toUriString(),
				HttpMethod.PUT,
				entity,
				ProjectDto.class);
		return exchange.getBody();
	}

	private String buildForkedRepositoryName(Long courseEditionId, ProjectDto projectDto) {
		return projectDto.getName() + "_" + courseEditionId + "_" + getTimestamp();
	}

	private String getTimestamp(){
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		return dateFormat.format(new Date());
	}

	private void createGitHook(Long projectId) {
		String resourceUrl = gitlabApiProjects + projectId + HOOKS;
		UriComponentsBuilder builder = createComponentBuilder(resourceUrl)
				.queryParam(URL, gitEventEndpoint)
				.queryParam(PUSH_EVENTS, Boolean.TRUE);
		HttpEntity<?> entity = getHttpEntity();


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

	private HttpEntity<?> getHttpEntity() {
		HttpHeaders headers = getHttpHeaders();
		return new HttpEntity<>(headers);
	}
}
