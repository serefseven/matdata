package com.logicpeaks.security.controller;

import com.logicpeaks.security.persistence.dto.TenantCheckDto;
import com.logicpeaks.security.service.TenantService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/tenant")
public class TenantController {

    TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping("/check/{tenant_id}")
    public TenantCheckDto checkTenant(@PathVariable String tenant_id) throws Exception {
        return tenantService.checkTenant(tenant_id);
    }

}
