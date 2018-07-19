package pl.coddlers.core.models.entity;

public enum TaskStatus {
    NOT_SUBMITTED("NOT SUBMITTED"),
    WAITING_FOR_REVIEW("WAITING FOR REVIEW"),
    GRADED("GRADED"),
    CHANGES_REQUESTED("CHANGES REQUESTED");

    private final String status;

    TaskStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
