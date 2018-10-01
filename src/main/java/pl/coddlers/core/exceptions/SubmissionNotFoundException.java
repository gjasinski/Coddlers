package pl.coddlers.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubmissionNotFoundException extends RuntimeException {
    public SubmissionNotFoundException(String branchName, String repoName) {
        super(String.format("Not found submission with branch name %s and repo name %s", branchName, repoName));
    }
}
