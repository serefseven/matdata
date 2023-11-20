package com.logicpeaks.security.exception;


import com.logicpeaks.security.enums.ApiExceptionType;

public class LoginException extends ApiException {


    public LoginException(String... params) {
        super(ApiError.builder()
                .apiErrorMessage(String.format(ApiExceptionType.LOGIN_FAIL.getMessage(), params))
                .apiExceptionType(ApiExceptionType.LOGIN_FAIL)
                .build());
    }
}
