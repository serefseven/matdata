package com.logicpeaks.security.service;

import com.logicpeaks.security.exception.DataNotFoundException;
import com.logicpeaks.security.persistence.dto.PermissionDtoApiResponse;
import com.logicpeaks.security.persistence.dto.RoleDtoApiResponse;
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

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private static final Logger log = LoggerFactory.getLogger(PermissionService.class);
    @NonNull
    private PermissionRepository permissionRepository;
    @NonNull
    private final ModelMapper modelMapper;

    public List<PermissionDtoApiResponse> getList() {
        List<PermissionEntity> permissionList = permissionRepository.findAll(Sort.by(Sort.Direction.ASC,"description"));
        return permissionList.stream().map(r -> modelMapper.map(r, PermissionDtoApiResponse.class)).collect(Collectors.toList());
    }
}
