package com.logicpeaks.security.persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignRoleToUserRequest {

    private Long roleId;
    private Long userId;

}
