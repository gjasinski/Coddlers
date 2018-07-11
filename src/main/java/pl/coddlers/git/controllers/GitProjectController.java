package pl.coddlers.git.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coddlers.git.services.GitProjectService;
import pl.coddlers.git.services.GitTaskService;
import pl.coddlers.git.services.GitUserService;


//@RestController
//@RequestMapping("/api/git")
//this class is only for test purposes! uncoment when testing
@Deprecated
class GitProjectController {

	private final GitProjectService gitProjectService;
	private GitUserService gitUserService;
	private GitTaskService gitTaskService;

	@Autowired
	GitProjectController(GitProjectService gitProjectService, GitUserService gitUserService, GitTaskService gitTaskService) {
		this.gitProjectService = gitProjectService;
		this.gitUserService = gitUserService;
		this.gitTaskService = gitTaskService;
	}

	@PostMapping("/users")
	public HttpEntity<Long> createUser(@RequestParam String email, @RequestParam String name, @RequestParam String username, @RequestParam String password) {
		return new HttpEntity<>(this.gitUserService.createUser(email, name, username, password));
	}

	@PostMapping("/projects")
	public HttpEntity<Long> createProject(@RequestParam("userId") long userId,
	                                      @RequestParam("name") String name) {
		return new HttpEntity<>(this.gitProjectService.createCourse(userId, name));
	}

	@PostMapping("/tasks")
	public String createTask(@RequestParam("repositoryId") long repositoryId,
	                                      @RequestParam("name") String taskName) {
		this.gitTaskService.createTask(repositoryId, taskName);
		return "OK";
	}

	@PostMapping("/projects/fork")
	public HttpEntity<Long> forkProject(@RequestParam("courseId") Long courseId,
	                                    @RequestParam("studentId") Long studentId) {
		return new HttpEntity<>(this.gitProjectService.addStudentToCourse(courseId, studentId));
	}

}
