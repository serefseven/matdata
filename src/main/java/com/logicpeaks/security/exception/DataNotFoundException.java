package com.logicpeaks.security.exception;


import com.logicpeaks.security.enums.ApiExceptionType;

public class DataNotFoundException extends ApiException {


    public DataNotFoundException(String... params) {
        super(ApiError.builder()
                .apiErrorMessage(String.format(ApiExceptionType.DATA_NOT_FOUND.getMessage(), params))
                .apiExceptionType(ApiExceptionType.DATA_NOT_FOUND)
                .build());
    }
}
