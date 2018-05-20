package pl.coddlers.git.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/git")
class GitUserController {

	private final GitUserService gitUserService;

	@Autowired
	GitUserController(GitUserService gitUserService) {
		this.gitUserService = gitUserService;
	}

	@PostMapping("/users")
	public HttpEntity<String> createUser(@RequestParam("email") String email,
	                              @RequestParam("name") String name,
	                              @RequestParam("username") String username,
	                              @RequestParam("password") String password) {
		return this.gitUserService.createUser(email, name, username, password);
	}
}
