package com.logicpeaks.security.service;

import com.logicpeaks.security.exception.DataNotFoundException;
import com.logicpeaks.security.enums.TenantStatus;
import com.logicpeaks.security.persistence.dto.TenantCheckDto;
import com.logicpeaks.security.persistence.entity.TenantEntity;
import com.logicpeaks.security.persistence.repository.TenantRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TenantService {

    private static final Logger log = LoggerFactory.getLogger(TenantService.class);
    private final TenantRepository tenantRepository;
    private final ModelMapper modelMapper;

    public TenantCheckDto checkTenant(String tenantId) throws Exception {
        TenantEntity tenantEntity = tenantRepository.findByTenantIdAndStatusIn(tenantId,
                Arrays.asList(TenantStatus.ACTIVE,
                        TenantStatus.GRACE_PERIOD,
                        TenantStatus.WAITING_DEACTIVATION
                        ));

        if(Optional.ofNullable(tenantEntity).isPresent()){
            return modelMapper.map(tenantEntity, TenantCheckDto.class);
        } else {
            log.error(String.format("Tenant not found, id : %s", tenantId));
            throw new DataNotFoundException(tenantId);
        }
    }
}
