package pl.coddlers.core.models.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum SubmissionStatusTypeEnum {
    NOT_SUBMITTED("NOT_SUBMITTED"),
    WAITING_FOR_REVIEW("WAITING_FOR_REVIEW"),
    GRADED("GRADED"),
    CHANGES_REQUESTED("CHANGES_REQUESTED");

    private final String status;
    private static final Map<String, SubmissionStatusTypeEnum> valuesMap = new HashMap<>();

    static {
        valuesMap.put("NOT_SUBMITTED", NOT_SUBMITTED);
        valuesMap.put("WAITING_FOR_REVIEW", WAITING_FOR_REVIEW);
        valuesMap.put("GRADED", GRADED);
        valuesMap.put("CHANGES_REQUESTED", CHANGES_REQUESTED);
    }

    SubmissionStatusTypeEnum(String status) {
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }

    @JsonCreator
    public static SubmissionStatusTypeEnum getEnumByStatusName(String statusName) {
        return valuesMap.get(statusName);
    }
}
