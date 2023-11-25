package com.logicpeaks.security.enums;
import com.fasterxml.jackson.annotation.JsonValue;
public enum UserType {
    CLIENT,ADMIN;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
