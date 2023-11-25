package com.logicpeaks.security.persistence.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckPasswordResetTokenRequest {

    @NotBlank(message = "Email Name must not be blank.")
    private String email;

    @NotBlank(message = "Token Name must not be blank.")
    private String token;

}
