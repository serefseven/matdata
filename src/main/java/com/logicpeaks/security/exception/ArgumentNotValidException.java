package com.logicpeaks.security.exception;


import com.logicpeaks.security.enums.ApiExceptionType;

public class ArgumentNotValidException extends ApiException {


    public ArgumentNotValidException(String... params) {
        super(ApiError.builder()
                .apiErrorMessage(String.format(ApiExceptionType.ARGUMENT_NOT_VALID.getMessage(), params))
                .apiExceptionType(ApiExceptionType.ARGUMENT_NOT_VALID)
                .build());
    }
}
