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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class GitTaskService {
    private static final String MASTER_POSTFIX = "-master";
    private static final String DEVELOP = "-develop";
    private static final String REPOSITORY_ID = "repositoryId";
    private static final String BRANCH = "branch";
    private static final String REF = "ref";
    private static final String MASTER = "master";
    private static final String PRIVATE_TOKEN = "private_token";

    private final HookRepository hookRepository;

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${gitlab.api.host}:${gitlab.api.http.port}${gitlab.api.prefix}")
    private String gitlabApi;

    @Value("${gitlab.api.apiuser.private_token}")
    private String private_token;

    @Value("${pl.coddlers.git.http.timeout.milliseconds}")
    private long timeout;

    private ExecutorService executor = Executors.newFixedThreadPool(10);

    @Autowired
    public GitTaskService(HookRepository hookRepository) {
        this.hookRepository = hookRepository;
    }

    public void createTask(long repositoryId, String taskName) {
        Future<Boolean> develop = createBranch(repositoryId, taskName + DEVELOP);
        Future<Boolean> master = createBranch(repositoryId, taskName + MASTER_POSTFIX);
        try {
            Boolean createdDevelop = develop.get(timeout, TimeUnit.MILLISECONDS);
            Boolean createdMaster = master.get(timeout, TimeUnit.MILLISECONDS);
            if (createdDevelop && createdMaster) {
                registerHook(repositoryId, taskName);
            } else {
                log.debug("Cannot register hook - branch was not created");
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error(e.toString());
        }
    }

    private void registerHook(long repositoryId, String taskName) {
        hookRepository.save(createHook(repositoryId, taskName + MASTER_POSTFIX));
    }

    private Future<Boolean> createBranch(long repositoryId, String branchName) {
        return executor.submit(() -> {
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
        });
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private String createApiUrl(long repositoryId) {
        return gitlabApi + " /projects/" + repositoryId + "/repository/branches";
    }

    private Hook createHook(Long projectId, String branchName) {
        Hook hook = new Hook();
        hook.setProjectId(projectId);
        hook.setBranch(branchName);
        return hook;
    }

    private UriComponentsBuilder createComponentBuilder(String resourceUrl) {
        return UriComponentsBuilder.fromHttpUrl(resourceUrl)
                .queryParam(PRIVATE_TOKEN, private_token);
    }
}
