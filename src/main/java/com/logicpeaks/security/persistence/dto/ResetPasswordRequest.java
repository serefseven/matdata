package com.logicpeaks.security.persistence.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {

    @NotBlank(message = "User Name must not be blank.")
    private String username;

}
