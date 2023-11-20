package com.logicpeaks.security.persistence.repository;

import com.logicpeaks.security.enums.TenantStatus;
import com.logicpeaks.security.persistence.entity.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantRepository extends JpaRepository<TenantEntity,Long> {

    TenantEntity findByTenantIdAndStatusIn(String tenantId, List<TenantStatus> statuses);

}
