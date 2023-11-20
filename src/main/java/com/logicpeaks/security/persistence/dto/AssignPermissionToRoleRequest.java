package com.logicpeaks.security.persistence.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignPermissionToRoleRequest {

    private Long roleId;
    private Long permissionId;

}
