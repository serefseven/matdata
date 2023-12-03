package com.logicpeaks.security.matdata.persistence.repository;

import com.logicpeaks.security.matdata.persistence.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity,Long> {
}
