package com.logicpeaks.security.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ApiException extends Exception{

    private ApiError apiError;

    public ApiException(ApiError apiError) {
        super(apiError.getApiErrorMessage());
        this.apiError = apiError;
    }
}
