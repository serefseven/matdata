package com.logicpeaks.security.persistence.entity;

import com.logicpeaks.security.enums.TenantStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "tenant")
public class TenantEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tenant_seq")
    @SequenceGenerator(name = "tenant_seq", sequenceName = "tenant_seq")
    private Long id;

    @Column(name = "tenant_id")
    private String tenantId;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "next_renewal_date")
    private Date nextRenewalDate;
    @Column(name = "is_trial")
    private Boolean isTrial;
    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private TenantStatus status;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "commercial_title")
    private String commercialTitle;
    @Column(name = "company_logo_url")
    private String companyLogoUrl;

}
