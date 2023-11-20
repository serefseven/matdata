package com.logicpeaks.security.exception;

import com.logicpeaks.security.enums.ApiExceptionType;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiError {
    private String apiErrorMessage;
    private ApiExceptionType apiExceptionType;
}
