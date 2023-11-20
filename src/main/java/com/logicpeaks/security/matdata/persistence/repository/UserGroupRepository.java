package com.logicpeaks.security.matdata.persistence.repository;

import com.logicpeaks.security.matdata.persistence.entity.UserGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroupEntity,Long> {

}
