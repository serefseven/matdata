package com.logicpeaks.security.persistence.dto;

import com.logicpeaks.security.enums.TenantStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TenantCheckDto {

    private Long id;
    private String tenantId;
    private TenantStatus status;
    private String companyName;
    private String commercialTitle;
    private String companyLogoUrl;

}
