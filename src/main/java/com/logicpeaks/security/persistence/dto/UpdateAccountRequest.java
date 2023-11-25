package com.logicpeaks.security.persistence.dto;

import com.logicpeaks.security.enums.UserStatus;
import com.logicpeaks.security.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountRequest {

    @NotBlank(message = "Ad boş bırakılamaz.")
    private String firstName;
    @NotBlank(message = "Soyad boş bırakılamaz.")
    private String lastName;

}
