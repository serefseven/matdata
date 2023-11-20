package com.logicpeaks.security.matdata.persistence.repository;

import com.logicpeaks.security.matdata.persistence.entity.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity,Long> {

    List<ApplicationEntity> findByIdInOrderByName(List<Long> ids);
}
