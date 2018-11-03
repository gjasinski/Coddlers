package pl.coddlers.core.models.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum CommentTypeEnum {
    GENERAL_COMMENT("GENERAL_COMMENT"),
    REOPEN_REASON("REOPEN_REASON"),
    GRADE_COMMENT("GRADE_COMMENT"),
    REQUEST_CHANGES_REASON("REQUEST_CHANGES_REASON");

    private final String commentType;
    private  static final Map<String, CommentTypeEnum> valuesMap = new HashMap<>();

    static {
        valuesMap.put("GENERAL_COMMENT", GENERAL_COMMENT);
        valuesMap.put("REOPEN_REASON", REOPEN_REASON);
        valuesMap.put("GRADE_COMMENT", GRADE_COMMENT);
        valuesMap.put("REQUEST_CHANGES_REASON", REQUEST_CHANGES_REASON);
    }

    CommentTypeEnum(String commentType) {
        this.commentType = commentType;
    }

    @JsonValue
    public String getCommentType() {
        return commentType;
    }

    @JsonCreator
    public static CommentTypeEnum getEnumByCommentTypeName(String commentType) {
        return valuesMap.get(commentType);
    }
}
