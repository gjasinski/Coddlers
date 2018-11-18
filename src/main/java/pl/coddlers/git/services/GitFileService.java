package pl.coddlers.git.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.coddlers.git.models.GitFile;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class GitFileService {
    private static final String PRIVATE_TOKEN = "private_token";
    private static final String PROJECTS = "/projects/";
    private static final String REPOSITORY_TREE = "/repository/tree";
    private static final String REF = "ref";
    private static final String RECURSIVE = "recursive";
    private static final String PER_PAGE = "per_page";
    private static final String SEARCH_FILE_RECURSIVELY = "true";
    private static final int NUMBER_OF_FILES_PER_PAGE = 1000;

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${gitlab.api.host}:${gitlab.api.http.port}${gitlab.api.prefix}")
    private String gitlabApi;

    @Value("${gitlab.api.apiuser.private_token}")
    private String privateToken;

    private ExecutorService executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());

    public CompletableFuture<GitFile[]> getRepositoryFiles(Long gitRepositoryId, String branch) {
        return CompletableFuture.supplyAsync(() -> {
            String resourceUrl = gitlabApi + PROJECTS + gitRepositoryId + REPOSITORY_TREE;

            HttpHeaders headers = getHttpHeaders();
            UriComponentsBuilder builder = createComponentBuilder(resourceUrl)
                    .queryParam(REF, branch)
                    .queryParam(RECURSIVE, SEARCH_FILE_RECURSIVELY)
                    .queryParam(PER_PAGE, NUMBER_OF_FILES_PER_PAGE);


            HttpEntity<?> entity = new HttpEntity<>(headers);
            log.error(builder.build().toUriString());

            return restTemplate.exchange(
                    builder.build().toUriString(),
                    HttpMethod.GET,
                    entity,
                    GitFile[].class)
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
