package com.logicpeaks.security.persistence.repository;

import com.logicpeaks.security.enums.TenantStatus;
import com.logicpeaks.security.enums.UserStatus;
import com.logicpeaks.security.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByEmail(String email);
    UserEntity findByEmailAndStatus(String email, UserStatus status);
    UserEntity findByUsernameAndStatus(String username, UserStatus status);

}
