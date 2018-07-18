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

    private static Map<String, TaskStatus> stringTaskStatusMap = new HashMap<>();
    private String name;

    static {
        stringTaskStatusMap.put("NOT SUBMITTED", NOT_SUBMITTED);
        stringTaskStatusMap.put("WAITING FOR REVIEW", WAITING_FOR_REVIEW);
        stringTaskStatusMap.put("GRADED", GRADED);
        stringTaskStatusMap.put("CHANGES REQUESTED", CHANGES_REQUESTED);
    }

    TaskStatus(String name) {
        this.name = name;
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
