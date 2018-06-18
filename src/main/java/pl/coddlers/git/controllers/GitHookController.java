package pl.coddlers.git.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.coddlers.git.models.Hook;
import pl.coddlers.git.models.PushHookResponseDTO;
import pl.coddlers.git.reposiories.HookRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/git/hooks")
public class GitHookController {
	private static final Logger LOGGER = LoggerFactory.getLogger(GitHookController.class);
	private static final String OBJECT_KIND_PUSH = "push";
	private static final String REF_HEADS = "ref/heads/";

	private final HookRepository hookRepository;

	@Autowired
	public GitHookController(HookRepository hookRepository) {
		this.hookRepository = hookRepository;
	}


	@PostMapping("/push")
	public void pushHook(@RequestBody PushHookResponseDTO pushEvent) {
		LOGGER.debug("Received event hook: " + pushEvent.toString());
		if (isPushEvent(pushEvent) && isHookEnabled(pushEvent)) {
			LOGGER.info("Reacting on incoming push hook" + pushEvent);
			System.out.println("Reacting on incoming push hook" + pushEvent);
		}
	}

	private boolean isPushEvent(PushHookResponseDTO pushEvent) {
		return pushEvent.getObject_kind().equals(OBJECT_KIND_PUSH);
	}

	private boolean isHookEnabled(PushHookResponseDTO pushEvent) {
		String gitRef = pushEvent.getRef();
		String branch = gitRef.substring(REF_HEADS.length());
		Optional<Hook> byProjectIdAndBranch = hookRepository.getByProjectIdAndBranch(pushEvent.getProject_id(), branch);
		return byProjectIdAndBranch.isPresent();
	}


}
