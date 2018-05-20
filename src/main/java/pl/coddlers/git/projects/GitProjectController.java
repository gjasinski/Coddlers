package pl.coddlers.git.projects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/git")
class GitProjectController {

	private final GitProjectService gitProjectService;

	@Autowired
	GitProjectController(GitProjectService gitProjectService) {
		this.gitProjectService = gitProjectService;
	}

	@PostMapping("/projects")
	public HttpEntity<String> createUser(@RequestParam("userId") long userId,
	                                     @RequestParam("name") String name) {
		return this.gitProjectService.createUser(userId, name);
	}

	@PostMapping("/projects/fork")
	public HttpEntity<String> forkProject(@RequestParam("repository") String repository,
	                                      @RequestParam("namespace") String namespace) {
		return this.gitProjectService.forkProject(repository, namespace);
	}

}
