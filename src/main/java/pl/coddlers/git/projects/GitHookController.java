package pl.coddlers.git.projects;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/git/hooks")
public class GitHookController {

	@GetMapping
	public void someHook(@RequestParam("object_attributes") String str){
		System.out.println("RECEIVED HOOK" +  str);
	}


}
