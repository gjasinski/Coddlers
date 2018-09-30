package pl.coddlers.git.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.coddlers.git.models.event.EventDto;

@RestController
@RequestMapping("${pl.coddlers.git.event.url}")
public class GitHookController {
	private final Logger logger = LoggerFactory.getLogger(GitHookController.class);
	private static final String OBJECT_KIND_PUSH = "push";

	@PostMapping
	public void receiveEvent(@RequestBody EventDto event) {
		logger.debug("Received event hook: " + event.toString());
		if (isPushEvent(event) && event.getRef().contains("master")) {
			// TODO: 19.07.18 add logic
			logger.debug("push event, master merge");
		}
	}

	private boolean isPushEvent(EventDto pushEvent) {
		return pushEvent.getEventName().equals(OBJECT_KIND_PUSH);
	}
}
