package pl.coddlers.core.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum AccountTypeEnum {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_STUDENT("ROLE_STUDENT"),
    ROLE_TEACHER("ROLE_TEACHER");

    private String name;
    private static Map<String, AccountTypeEnum> valueMap = new HashMap<>();

    static {
        valueMap.put("ROLE_ADMIN", ROLE_ADMIN);
        valueMap.put("ROLE_STUDENT", ROLE_STUDENT);
        valueMap.put("ROLE_TEACHER", ROLE_TEACHER);
    }

    AccountTypeEnum(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static AccountTypeEnum getEnumByName(String name) {
        return valueMap.get(name);
    }
}
