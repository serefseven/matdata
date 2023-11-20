package com.logicpeaks.security.controller;

import com.logicpeaks.security.exception.DataNotFoundException;
import com.logicpeaks.security.persistence.dto.*;
import com.logicpeaks.security.service.RoleService;
import com.logicpeaks.security.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@AllArgsConstructor
public class RoleController {

    private RoleService roleService;

    @GetMapping
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.VIEW')")
    public List<RoleDtoApiResponse> getList() {
        return roleService.getList();
    }

    @GetMapping("/{roleId}/permission")
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.PERMISSION.VIEW')")
    public List<PermissionDtoApiResponse> getPermissionList(@PathVariable("roleId") Long roleId)
            throws DataNotFoundException {
        return roleService.getPermissionList(roleId);
    }

    @PostMapping
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.ADD')")
    public ResponseEntity<RoleDtoApiResponse> createRole(@Valid @RequestBody CreateRoleRequest createRoleRequest) {
        RoleDtoApiResponse roleDtoApiResponse = roleService.create(createRoleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleDtoApiResponse);
    }

    @PutMapping
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.UPDATE')")
    public ResponseEntity<RoleDtoApiResponse> updateRole(@Valid @RequestBody UpdateRoleRequest updateRoleRequest) {
        RoleDtoApiResponse roleDtoApiResponse = roleService.update(updateRoleRequest);
        return ResponseEntity.status(HttpStatus.OK).body(roleDtoApiResponse);
    }

    @DeleteMapping("{roleId}")
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.DELETE')")
    public ResponseEntity deleteRole(@PathVariable Long roleId) {
        roleService.delete(roleId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/permission")
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.PERMISSION.ADD')")
    public ResponseEntity assignPermissionToRole(@Valid @RequestBody AssignPermissionToRoleRequest request)
            throws DataNotFoundException {
        roleService.assignPermission(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @DeleteMapping("{roleId}/permission/{permissionId}")
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.DELETE')")
    public ResponseEntity unassignPermissionFromRole(@PathVariable Long roleId, @PathVariable Long permissionId)
            throws DataNotFoundException {
        roleService.unassignPermission(roleId, permissionId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
