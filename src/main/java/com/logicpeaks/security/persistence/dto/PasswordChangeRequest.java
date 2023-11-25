package com.logicpeaks.security.persistence.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequest {

    @Size(min = 6, message = "Kullanılan Parola en az 6 karakterden oluşmalıdır.")
    @NotBlank(message = "Kullanılan Parola boş bırakılamaz.")
    private String currentPassword;

    @Size(min = 6, message = "Parola en az 6 karakterden oluşmalıdır.")
    @NotBlank(message = "Parola boş bırakılamaz.")
    private String password;

    @NotBlank(message = "Parola Doğrulama boş bırakılamaz.")
    private String confirmPassword;

}
