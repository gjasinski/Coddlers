package pl.coddlers.git.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.models.entity.StudentLessonRepository;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.git.exceptions.GitErrorHandler;
import pl.coddlers.git.models.event.ProjectDto;
import pl.coddlers.git.models.event.TransferRepositoryDto;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@Slf4j
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
    private static final String PATH = "path";
    private static final String GROUPS = "/groups/";

    @Value("${pl.coddlers.git.host}:${pl.coddlers.git.port}${pl.coddlers.git.event.url}")
    private String gitEventEndpoint;

    @Value("${gitlab.api.host}:${gitlab.api.http.port}${gitlab.api.prefix}" + "/")
    private String gitlabApi;

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

    public CompletableFuture<ProjectDto> transferRepositoryToGroup(long projectId, long groupId) {
        return CompletableFuture.supplyAsync(() -> {
            String resourceUrl = gitlabApi + GROUPS + groupId + PROJECTS + projectId;
            UriComponentsBuilder builder = createComponentBuilder(resourceUrl);
            HttpEntity<?> entity = getHttpEntity();

            TransferRepositoryDto body = restTemplate.exchange(
                    builder.build().toUriString(),
                    HttpMethod.POST,
                    entity,
                    TransferRepositoryDto.class)
                    .getBody();
            ProjectDto[] projectsDto = body.getProjectsDto();
            for (int i = 0; i < projectsDto.length; i++) {
                ProjectDto project = projectsDto[i];
                if (project.getId() == projectId) {
                    return project;
                }
            }
            throw new IllegalArgumentException("In response should be project which was transferred");
        }, executor);
    }

    public CompletableFuture<ProjectDto> forkLesson(Lesson lesson, User user) {
        return CompletableFuture.supplyAsync(forkLessonSupplier(lesson.getGitProjectId(), user), executor)
                .thenApply(projectDto -> {
                    removeForkRelationship(projectDto);
                    createGitHook(projectDto.getId());
                    return projectDto;
                });
    }

    private Supplier<ProjectDto> forkLessonSupplier(Long lessonId, User user) {
        return () -> {
            String resourceUrl = gitlabApiProjects + lessonId + FORK;
            UriComponentsBuilder builder = createComponentBuilder(resourceUrl)
                    .queryParam(NAMESPACE, user.getUserMail().replace("@", "_at_"));
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

    public ProjectDto renameForkedRepository(Long gitProjectId, String repositoryName) {
        String resourceUrl = gitlabApiProjects + gitProjectId;
        UriComponentsBuilder builder = createComponentBuilder(resourceUrl)
                .queryParam(ID, gitProjectId)
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

    public StudentLessonRepository createStudentLessonRepository(CourseEdition courseEdition, User user, Lesson lesson, ProjectDto projectDto) {
        StudentLessonRepository studentLessonRepository = new StudentLessonRepository();
        studentLessonRepository.setCourseEdition(courseEdition);
        studentLessonRepository.setLesson(lesson);
        studentLessonRepository.setUser(user);
        studentLessonRepository.setGitRepositoryId(projectDto.getId());
        studentLessonRepository.setRepositoryUrl(projectDto.getPathWithNamespace());
        return studentLessonRepository;
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
