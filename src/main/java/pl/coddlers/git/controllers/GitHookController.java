package pl.coddlers.git.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.coddlers.core.models.dto.SubmissionDto;
import pl.coddlers.core.models.entity.Submission;
import pl.coddlers.core.services.SubmissionService;
import pl.coddlers.git.models.event.EventDto;

import static pl.coddlers.core.models.entity.SubmissionStatusTypeEnum.CHANGES_REQUESTED;
import static pl.coddlers.core.models.entity.SubmissionStatusTypeEnum.NOT_SUBMITTED;
import static pl.coddlers.core.models.entity.SubmissionStatusTypeEnum.WAITING_FOR_REVIEW;

@Slf4j
@RestController
@RequestMapping("${pl.coddlers.git.event.url}")
public class GitHookController {
	private static final String OBJECT_KIND_PUSH = "push";
	private SubmissionService submissionService;

	@Autowired
	public GitHookController(SubmissionService submissionService) {
		this.submissionService = submissionService;
	}

	@PostMapping
	public void receiveEvent(@RequestBody EventDto event) {
		log.debug("Received event hook: " + event.toString());
		if (isPushEvent(event) && event.getRef().contains("master")) {
			String branchName = extractBranchName(event);
			String repoUrl = event.getRepositoryDto().getGitHttpUrl();
			log.debug(String.format("Received push event. Pushed to %s in %s", branchName, repoUrl));

			SubmissionDto submissionDto = submissionService.getSubmissionByBranchNameAndRepoName(branchName, repoUrl);
			if (submissionDto.getSubmissionStatusTypeEnum() == NOT_SUBMITTED ||
			 	submissionDto.getSubmissionStatusTypeEnum() ==  CHANGES_REQUESTED) {
				submissionDto.setSubmissionStatusTypeEnum(WAITING_FOR_REVIEW);
				submissionService.updateSubmission(submissionDto);
			}
		}
	}

	private String extractBranchName(EventDto eventDto) {
		String[] tokens = eventDto.getRef().split("/");
		String branchName = tokens[tokens.length-1];
		return branchName.replace("-master", "");

	}

	private boolean isPushEvent(EventDto pushEvent) {
		return pushEvent.getEventName().equals(OBJECT_KIND_PUSH);
	}
}
