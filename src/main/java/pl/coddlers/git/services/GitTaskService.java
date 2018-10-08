package pl.coddlers.git.services;

import lombok.extern.slf4j.Slf4j;
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
import pl.coddlers.git.models.Hook;
import pl.coddlers.git.reposiories.HookRepository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

@Slf4j
@Service
public class GitTaskService {
    private static final String MASTER_POSTFIX = "-master";
    private static final String DEVELOP_POSTFIX = "-develop";
    private static final String REPOSITORY_ID = "repositoryId";
    private static final String BRANCH = "branch";
    private static final String REF = "ref";
    private static final String MASTER = "master";
    private static final String PRIVATE_TOKEN = "private_token";
    private static final String PROJECTS = "/projects/";
    private static final String REPOSITORY_BRANCHES = "/repository/branches";

    private final HookRepository hookRepository;

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${gitlab.api.host}:${gitlab.api.http.port}${gitlab.api.prefix}")
    private String gitlabApi;

    @Value("${gitlab.api.apiuser.private_token}")
    private String privateToken;

    @Value("${pl.coddlers.git.http.timeout.milliseconds}")
    private long timeout;

    private ExecutorService executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());

    @Autowired
    public GitTaskService(HookRepository hookRepository) {
        this.hookRepository = hookRepository;
    }

    public CompletableFuture<Boolean> createTask(long repositoryId, String taskName) {
        return CompletableFuture.supplyAsync(createTaskSupplier(repositoryId, taskName), executor);
    }

    private Supplier<Boolean> createTaskSupplier(long repositoryId, String taskName) {
        return () -> {
            String masterName = String.format("%s%s", taskName, MASTER_POSTFIX);
            String developName = String.format("%s%s", taskName, DEVELOP_POSTFIX);
            try {
                Boolean createdMaster = createBranch(repositoryId, masterName).get(timeout, TimeUnit.MILLISECONDS);
                Boolean createdDevelop = createBranch(repositoryId, developName).get(timeout, TimeUnit.MILLISECONDS);
                if (createdDevelop && createdMaster) {
                    return true;
                } else {
                    log.debug("Branches were not created");
                    return false;
                }
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                log.error(e.toString(), e.getCause());
                return false;
            }
        };
    }

    private CompletableFuture<Boolean> createBranch(long repositoryId, String branchName) {
        return CompletableFuture.supplyAsync(createBranchSupplier(repositoryId, branchName), executor);
    }

    private Supplier<Boolean> createBranchSupplier(long repositoryId, String branchName) {
        return () -> {
            String resourceUrl = createApiUrl(repositoryId);

            HttpHeaders headers = createHttpHeaders();

            UriComponentsBuilder builder = createComponentBuilder(resourceUrl)
                    .queryParam(REPOSITORY_ID, repositoryId)
                    .queryParam(BRANCH, branchName)
                    .queryParam(REF, MASTER);


            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<String> exchange = restTemplate.exchange(
                    builder.build().toUriString(),
                    HttpMethod.POST,
                    entity,
                    String.class);
            if (!exchange.getStatusCode().is2xxSuccessful()) {
                log.debug(exchange.getStatusCode().toString());
            }
            return exchange.getStatusCode().is2xxSuccessful();
        };
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private String createApiUrl(long repositoryId) {
        return gitlabApi + PROJECTS + repositoryId + REPOSITORY_BRANCHES;
    }

    private UriComponentsBuilder createComponentBuilder(String resourceUrl) {
        return UriComponentsBuilder.fromHttpUrl(resourceUrl)
                .queryParam(PRIVATE_TOKEN, privateToken);
    }
}
