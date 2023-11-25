package com.logicpeaks.security.persistence.repository;

import com.logicpeaks.security.persistence.entity.PasswordResetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordResetEntity,Long> {

    public Optional<PasswordResetEntity> findByToken(String token);

}
