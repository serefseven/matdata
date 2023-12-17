package com.logicpeaks.security.persistence.dto;

import com.logicpeaks.security.enums.UserPasswordSettingTypes;
import com.logicpeaks.security.enums.UserStatus;
import com.logicpeaks.security.enums.UserType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    private String username;
    @Email(message = "Geçersiz e-mail adresi.")
    @NotBlank(message = "E-mail boş bırakılamaz.")
    private String email;

    //@Size(min = 6, message = "Parola en az 6 karakterden oluşmalıdır.")
    //@NotBlank(message = "Parola boş bırakılamaz.")
    private String password;
    //@NotBlank(message = "Parola Doğrulama boş bırakılamaz.")
    private String confirmPassword;
    @NotBlank(message = "Ad boş bırakılamaz.")
    private String firstName;
    @NotBlank(message = "Soyad boş bırakılamaz.")
    private String lastName;
    private UserPasswordSettingTypes passwordSettingType;
    private UserStatus status;
    private Long userGroupId;
    private UserType type;
    private Boolean sendEmail;

}
