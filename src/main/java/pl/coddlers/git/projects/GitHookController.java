package pl.coddlers.git.projects;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/git/hooks")
public class GitHookController {

	@PostMapping
	public void someHook(@RequestBody Object o){
		System.out.println("RECEIVED HOOK" +  o.toString());
	}


}
