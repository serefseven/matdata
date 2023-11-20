package com.logicpeaks.security.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiExceptionType {

    DATA_NOT_FOUND("DATA_NOT_FOUND", "%s not found.", HttpStatus.NOT_FOUND),
    LOGIN_FAIL("LOGIN_FAIL", "Wrong username or password.", HttpStatus.UNAUTHORIZED),
    ARGUMENT_NOT_VALID("ARGUMENT_NOT_VALID", "%s", HttpStatus.UNPROCESSABLE_ENTITY)

    ;

    private final String messageCode;
    private final String message;
    private final HttpStatus status;

}
