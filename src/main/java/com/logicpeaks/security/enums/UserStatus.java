package com.logicpeaks.security.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserStatus {
    ACTIVE, DEACTIVE;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
