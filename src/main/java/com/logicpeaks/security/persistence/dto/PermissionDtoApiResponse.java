package com.logicpeaks.security.persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionDtoApiResponse {

    private Long id;
    private String permission;
    private String description;

}
