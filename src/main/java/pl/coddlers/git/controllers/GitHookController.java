package pl.coddlers.git.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.coddlers.core.models.dto.SubmissionDto;
import pl.coddlers.core.services.SubmissionService;
import pl.coddlers.git.exceptions.GitBadRequestException;
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
			if (event.getRepositoryDto() == null) {
				throw new GitBadRequestException("There is no repository info");
			}
			String branchName = extractBranchName(event);
			String repoUrl = extractRepoName(event.getRepositoryDto().getGitHttpUrl());
			log.info(String.format("Received push event. Pushed to %s in %s", branchName, repoUrl));

			SubmissionDto submissionDto = submissionService.getSubmissionByBranchNameAndRepoName(branchName, repoUrl);
			if (submissionDto.getSubmissionStatusType() == NOT_SUBMITTED ||
			 	submissionDto.getSubmissionStatusType() ==  CHANGES_REQUESTED) {
				submissionDto.setSubmissionStatusType(WAITING_FOR_REVIEW);
				submissionService.updateSubmission(submissionDto);
			}
		}
	}

	private String extractRepoName(String repoUrl) {
		repoUrl = repoUrl.replace("http://", "")
				.replace("https://", "");
		return repoUrl.substring(repoUrl.indexOf("/")+1, repoUrl.length()-4);
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
