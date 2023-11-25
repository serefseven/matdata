package com.logicpeaks.security.persistence.dto;

import com.logicpeaks.security.enums.UserStatus;
import com.logicpeaks.security.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    private Long id;
    private String username;
    @Email(message = "Geçersiz e-mail adresi.")
    private String email;
    @Size(min = 6, message = "Parola en az 6 karakterden oluşmalıdır.")
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private UserStatus status;
    private Long userGroupId;
    private UserType type;
}
