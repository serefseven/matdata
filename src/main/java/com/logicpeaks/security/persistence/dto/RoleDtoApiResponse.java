package com.logicpeaks.security.persistence.dto;

import com.logicpeaks.security.enums.TenantStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDtoApiResponse {

    private Long id;
    private String role;
    private String description;

}
