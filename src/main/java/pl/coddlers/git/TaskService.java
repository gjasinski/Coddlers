package pl.coddlers.git;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.coddlers.git.models.Hook;
import pl.coddlers.git.reposiories.HookRepository;

@Service
public class TaskService {
	public static final String MASTER = "-master";
	public static final String DEVELOP = "-develop";
	private RestTemplate restTemplate = new RestTemplate();

	@Value("${gitlab.api.host}:${gitlab.api.http.port}${gitlab.api.prefix}")
	private String gitlabApi;

	@Value("${gitlab.api.apiuser.private_token}")
	private String private_token;

	private final HookRepository hookRepository;

	@Autowired
	public TaskService(HookRepository hookRepository) {
		this.hookRepository = hookRepository;
	}

	public void createTask(long repositoryId, String taskName){
		createBranch(repositoryId, taskName + DEVELOP);
		createBranch(repositoryId, taskName + MASTER);
		hookRepository.save(createHook(repositoryId, taskName + MASTER));
	}

	private void createBranch(long repositoryId, String branchName) {
		String resourceUrl = createApiUrl(repositoryId);

		HttpHeaders headers = createHttpHeaders();

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(resourceUrl)
				.queryParam("private_token", private_token)
				.queryParam("repositoryId", repositoryId)
				.queryParam("branch", branchName)
				.queryParam("ref", "master");


		HttpEntity<?> entity = new HttpEntity<>(headers);

		restTemplate.exchange(
				builder.build().toUriString(),
				HttpMethod.POST,
				entity,
				String.class);
	}

	private HttpHeaders createHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

	private String createApiUrl(long repositoryId) {
		return gitlabApi + " /projects/" + repositoryId + "/repository/branches";
	}

	private Hook createHook(Long projectId, String branchName){
		Hook hook = new Hook();
		hook.setProjectId(projectId);
		hook.setBranch(branchName);
		return hook;
	}
}
