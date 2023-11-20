package com.logicpeaks.security.persistence.repository;

import com.logicpeaks.security.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,Long> {

}
