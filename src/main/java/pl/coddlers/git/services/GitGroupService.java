package pl.coddlers.git.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.coddlers.git.models.event.ProjectDto;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class GitGroupService {
    private static final String GROUPS = "/groups/";
    private static final String PATH = "path";
    private static final String NAME = "name";
    private static final String PRIVATE = "private";
    private static final String VISIBILITY = "visibility";
    private static final String PRIVATE_TOKEN = "private_token";
    private static final String MEMBERS = "/members";
    private static final String ID = "id";
    private static final String USER_ID = "user_id";
    private static final String ACCESS_LEVEL = "access_level";
    private static final int MAINTAINER_ACCESS = 40;

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${gitlab.api.host}:${gitlab.api.http.port}${gitlab.api.prefix}")
    private String gitlabApi;

    @Value("${gitlab.api.apiuser.private_token}")
    private String privateToken;

    private ExecutorService executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());

    public CompletableFuture<ProjectDto> createGroup(String name) {
        return CompletableFuture.supplyAsync(() -> {
            String resourceUrl = gitlabApi + GROUPS;

            HttpHeaders headers = getHttpHeaders();
            UriComponentsBuilder builder = createComponentBuilder(resourceUrl)
                    .queryParam(NAME, name)
                    .queryParam(PATH, name)
                    .queryParam(VISIBILITY, PRIVATE);


            HttpEntity<?> entity = new HttpEntity<>(headers);

            return restTemplate.exchange(
                    builder.build().toUriString(),
                    HttpMethod.POST,
                    entity,
                    ProjectDto.class)
                    .getBody();
        }, executor);
    }

    public CompletableFuture<Void> addUserToGroup(Long gitUserId, Long gitGroupId) {
        return CompletableFuture.runAsync(() -> {
            String resourceUrl = gitlabApi + GROUPS + gitGroupId + MEMBERS;

            HttpHeaders headers = getHttpHeaders();
            UriComponentsBuilder builder = createComponentBuilder(resourceUrl)
                    .queryParam(USER_ID, gitUserId)
                    .queryParam(ACCESS_LEVEL, MAINTAINER_ACCESS);


            HttpEntity<?> entity = new HttpEntity<>(headers);

            restTemplate.exchange(
                    builder.build().toUriString(),
                    HttpMethod.POST,
                    entity,
                    Object.class)
                    .getBody();
        }, executor);
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
