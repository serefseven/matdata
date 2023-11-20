package com.logicpeaks.security.controller;

import com.logicpeaks.security.persistence.dto.PermissionDtoApiResponse;
import com.logicpeaks.security.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permission")
@AllArgsConstructor
public class PermissionController {

    private PermissionService permissionService;

    @GetMapping
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.PERMISSION.VIEW')")
    public List<PermissionDtoApiResponse> getList() {
        return permissionService.getList();
    }

}
