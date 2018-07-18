package pl.coddlers.core.models.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum TaskStatus {
    NOT_SUBMITTED("NOT SUBMITTED"),
    WAITING_FOR_REVIEW("WAITING FOR REVIEW"),
    GRADED("GRADED"),
    CHANGES_REQUESTED("CHANGES REQUESTED");

    private final static Map<String, TaskStatus> stringTaskStatusMap = new HashMap<>();

    private final String name;

    TaskStatus(String name) {
        this.name = name;
    }

    static {
        stringTaskStatusMap.put(NOT_SUBMITTED.toName(), NOT_SUBMITTED);
        stringTaskStatusMap.put(WAITING_FOR_REVIEW.toName(), WAITING_FOR_REVIEW);
        stringTaskStatusMap.put(GRADED.toName(), GRADED);
        stringTaskStatusMap.put(CHANGES_REQUESTED.toName(), CHANGES_REQUESTED);
    }

    @JsonCreator
    public static TaskStatus fromName(String name) {
        return stringTaskStatusMap.get(name);
    }

    @JsonValue
    public String toName() {
        return name;
    }
}
