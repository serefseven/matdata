package com.logicpeaks.security.exception.handler;

import com.logicpeaks.security.enums.ApiExceptionType;
import com.logicpeaks.security.enums.ApiStatusType;
import com.logicpeaks.security.exception.ApiException;
import com.logicpeaks.security.persistence.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceiptionHandler {

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity handleConflict(ApiException ex) {
        return ResponseEntity
                .status(ex.getApiError().getApiExceptionType().getStatus())
                .body(ApiResponse
                        .builder()
                        .status(ApiStatusType.ERROR)
                        .error(ApiResponse.Errors.builder()
                                .code(ex.getApiError().getApiExceptionType().getMessageCode())
                                .message(ex.getMessage())
                                .build())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.joining(" "));

        return ResponseEntity
                .status(ApiExceptionType.ARGUMENT_NOT_VALID.getStatus())
                .body(ApiResponse
                        .builder()
                        .status(ApiStatusType.ERROR)
                        .error(ApiResponse.Errors.builder()
                                .code(ApiExceptionType.ARGUMENT_NOT_VALID.getMessageCode())
                                .message(String.format(ApiExceptionType.ARGUMENT_NOT_VALID.getMessage(), errors))
                                .build())
                        .build());
    }

}
