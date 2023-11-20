package com.logicpeaks.security.persistence.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoleRequest {

    private Long id;
    @NotBlank(message = "Role Code must not be blank.")
    private String role;
    @NotBlank(message = "Role must not be blank.")
    private String description;

}
