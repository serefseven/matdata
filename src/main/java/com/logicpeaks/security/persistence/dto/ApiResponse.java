package com.logicpeaks.security.persistence.dto;

import com.logicpeaks.security.enums.ApiStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class ApiResponse<T> {

    private ApiStatusType status;
    private T data;

    private Errors error;

    @Getter
    @Setter
    @Builder
    public static class Errors{
        private String code;
        private String message;
    }
}
