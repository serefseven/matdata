package com.logicpeaks.security.persistence.dto;

import com.logicpeaks.security.enums.TenantStatus;
import com.logicpeaks.security.persistence.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TenantDto {

    private Long id;
    private String tenantId;
    private Date startDate;
    private Date nextRenewalDate;
    private Boolean isTrial;
    private TenantStatus status;
    private String companyName;
    private String commercialTitle;
    private String companyLogoUrl;

}
