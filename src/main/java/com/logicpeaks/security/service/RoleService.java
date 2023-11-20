package com.logicpeaks.security.service;

import com.logicpeaks.security.exception.DataNotFoundException;
import com.logicpeaks.security.persistence.dto.*;
import com.logicpeaks.security.persistence.entity.PermissionEntity;
import com.logicpeaks.security.persistence.entity.RoleEntity;
import com.logicpeaks.security.persistence.repository.PermissionRepository;
import com.logicpeaks.security.persistence.repository.RoleRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleService.class);
    @NonNull
    private RoleRepository roleRepository;
    @NonNull
    private PermissionRepository permissionRepository;
    @NonNull
    private final ModelMapper modelMapper;

    public List<RoleDtoApiResponse> getList() {
        List<RoleEntity> roleList = roleRepository.findAll(Sort.by(Sort.Direction.ASC,"description"));
        return roleList.stream().map(r -> modelMapper.map(r, RoleDtoApiResponse.class)).collect(Collectors.toList());
    }

    public List<PermissionDtoApiResponse> getPermissionList(Long roleId) throws DataNotFoundException {
        Optional<RoleEntity> role = roleRepository.findById(roleId);
        if(!role.isPresent()){
            log.error(String.format("Role not found, id : %s", roleId));
            throw new DataNotFoundException("Role");
        }

        return role.get()
                .getPermissions()
                .stream()
                .map(r -> modelMapper.map(r, PermissionDtoApiResponse.class))
                .sorted(Comparator.comparing(PermissionDtoApiResponse::getDescription))
                .collect(Collectors.toList());
    }

    public RoleDtoApiResponse create(CreateRoleRequest createRoleRequest) {
        RoleEntity roleEntity = modelMapper.map(createRoleRequest, RoleEntity.class);
        roleEntity = roleRepository.save(roleEntity);
        log.info(String.format("Role create successful, data : %s",roleEntity));
        return modelMapper.map(roleEntity, RoleDtoApiResponse.class);
    }

    public RoleDtoApiResponse update(UpdateRoleRequest updateRoleRequest) {
        RoleEntity roleEntity = modelMapper.map(updateRoleRequest, RoleEntity.class);
        roleEntity = roleRepository.save(roleEntity);
        log.info(String.format("Role update successful, data :  %s",roleEntity));
        return modelMapper.map(roleEntity, RoleDtoApiResponse.class);
    }

    public void delete(Long roleId) {
        roleRepository.deleteById(roleId);
        log.info(String.format("Role delete successful, id : %s",roleId));
    }

    public void assignPermission(AssignPermissionToRoleRequest request) throws DataNotFoundException {
        Optional<RoleEntity> role = roleRepository.findById(request.getRoleId());
        Optional<PermissionEntity> permisson = permissionRepository.findById(request.getPermissionId());

        if(!role.isPresent()){
            log.error(String.format("Role not found, id : %s", request.getRoleId()));
            throw new DataNotFoundException("Role");
        }
        if(!permisson.isPresent()){
            log.error(String.format("Permission not found, id : %s", request.getPermissionId()));
            throw new DataNotFoundException("Permission");
        }

        if(!Optional.ofNullable(role.get().getPermissions()).isPresent()) {
            role.get().setPermissions(new HashSet<>());
        }

        role.get().getPermissions().add(permisson.get());

        roleRepository.save(role.get());
        log.info(String.format("Role permission assign successful, role id : %s permission id : %s",
                request.getRoleId(),request.getPermissionId()));
    }

    public void unassignPermission(Long roleId, Long permissionId) throws DataNotFoundException {
        Optional<RoleEntity> role = roleRepository.findById(roleId);

        if(!role.isPresent()){
            log.error(String.format("Role not found, id : %s", roleId));
            throw new DataNotFoundException("Role");
        }

        if(!Optional.ofNullable(role.get().getPermissions()).isPresent()) {
            role.get().setPermissions(new HashSet<>());
        }
        Set<PermissionEntity> permissions = role.get().getPermissions();

        if(!permissions.stream().anyMatch(l -> l.getId().equals(permissionId))){
            log.error(String.format("Permission not found, id : %s", permissionId));
            throw new DataNotFoundException("Permission");
        }

        role.get().setPermissions(permissions
                .stream()
                .filter(p -> !p.getId().equals(permissionId))
                .collect(Collectors.toSet()));

        roleRepository.save(role.get());
        log.info(String.format("Role permission unassign successful, role id : %s permission id : %s",
                roleId, permissionId));
    }

}
