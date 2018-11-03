package pl.coddlers.core.exceptions;

public class NotSuchCommentTypeSpecifiedException extends Exception {
    public NotSuchCommentTypeSpecifiedException(String className) {
        super(String.format("Not defined commentType for %s class", className));
    }
}
