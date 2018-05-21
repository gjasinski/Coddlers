package pl.coddlers.git.projects;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/git/hooks")
public class GitHookController {

	@PostMapping
	public void someHook(@RequestParam("object_attributes") String str){
		System.out.println("RECEIVED HOOK" +  str);
	}


}
