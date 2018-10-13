package pl.coddlers.core.exceptions;

public class CouldNotCreateGitTaskException extends Exception {
    public CouldNotCreateGitTaskException(String branchPrefix) {
        super(String.format("Could not create branches with prefix %s for task purposes", branchPrefix));
    }
}
