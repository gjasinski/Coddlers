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
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.models.entity.StudentLessonRepository;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.git.exceptions.GitErrorHandler;
import pl.coddlers.git.models.event.ProjectDto;

import java.time.Instant;
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
    public static final String PATH = "path";

    @Value("${pl.coddlers.git.host}:${pl.coddlers.git.port}${pl.coddlers.git.event.url}")
    private String gitEventEndpoint;

    @Value("${gitlab.api.host}:${gitlab.api.http.port}${gitlab.api.prefix}" + "/" + PROJECTS + "/")
    private String gitlabApiProjects;

    @Value("${gitlab.api.apiuser.private_token}")
    private String privateToken;

    private RestTemplate restTemplate;

    private ExecutorService executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());

    @Autowired
    public GitLessonService() {
        this.restTemplate = new RestTemplate();
        this.restTemplate.setErrorHandler(new GitErrorHandler());
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

            return restTemplate.exchange(
                    builder.build().toUriString(),
                    HttpMethod.POST,
                    entity,
                    ProjectDto.class)
                    .getBody();
        };
    }

    public CompletableFuture<StudentLessonRepository> forkLesson(Lesson lesson, User user, CourseEdition courseEdition) {
        return CompletableFuture.supplyAsync(forkLessonSupplier(lesson.getGitProjectId(), user.getGitUserId()), executor)
                .thenApply(projectDto -> {
                    createGitHook(projectDto.getId());
                    removeForkRelationship(projectDto);
                    return projectDto;
                })
                .thenApply(projectDto -> renameForkedRepository(courseEdition.getId(), lesson.getId(), user.getId(), projectDto))
                .thenApply(projectDto -> createStudentLessonRepository(courseEdition, user, lesson, projectDto));
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

    private void removeForkRelationship(ProjectDto projectDto) {
        String resourceUrl = gitlabApiProjects + projectDto.getId() + "/fork";
        UriComponentsBuilder builder = createComponentBuilder(resourceUrl);
        HttpEntity<?> entity = getHttpEntity();

        ResponseEntity<Object> exchange = restTemplate.exchange(
                builder.build().toUriString(),
                HttpMethod.DELETE,
                entity,
                Object.class);
    }

    private ProjectDto renameForkedRepository(Long courseEditionId, Long lessonId, Long studentId, ProjectDto projectDto) {
        String repositoryName = buildForkedRepositoryName(courseEditionId, lessonId, studentId);
        String resourceUrl = gitlabApiProjects + projectDto.getId();
        UriComponentsBuilder builder = createComponentBuilder(resourceUrl)
                .queryParam(ID, projectDto.getId())
                .queryParam(NAME, repositoryName)
                .queryParam(PATH, repositoryName);
        HttpEntity<?> entity = getHttpEntity();

        ResponseEntity<ProjectDto> exchange = restTemplate.exchange(
                builder.build().toUriString(),
                HttpMethod.PUT,
                entity,
                ProjectDto.class);
        return exchange.getBody();
    }

    private StudentLessonRepository createStudentLessonRepository(CourseEdition courseEdition, User user, Lesson lesson, ProjectDto projectDto) {
        StudentLessonRepository studentLessonRepository = new StudentLessonRepository();
        studentLessonRepository.setCourseEdition(courseEdition);
        studentLessonRepository.setLesson(lesson);
        studentLessonRepository.setUser(user);
        studentLessonRepository.setGitRepositoryId(projectDto.getId());
        studentLessonRepository.setRepositoryUrl(projectDto.getPathWithNamespace());
        return studentLessonRepository;
    }

    private String buildForkedRepositoryName(Long courseEditionId, Long lessonId, Long studentId) {
        String timestamp = Long.toString(Instant.now().getEpochSecond());
        return courseEditionId + "_" + lessonId + "_" + studentId + "_" + timestamp;
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
