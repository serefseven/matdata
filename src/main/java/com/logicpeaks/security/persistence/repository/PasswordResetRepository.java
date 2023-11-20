package com.logicpeaks.security.persistence.repository;

import com.logicpeaks.security.persistence.entity.PasswordResetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordResetEntity,Long> {


}
