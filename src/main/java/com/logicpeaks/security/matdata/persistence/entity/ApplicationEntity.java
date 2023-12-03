package com.logicpeaks.security.matdata.persistence.entity;

import com.logicpeaks.security.persistence.entity.RoleEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "application")
public class ApplicationEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_seq")
    @SequenceGenerator(name = "application_seq", sequenceName = "application_seq", allocationSize = 1)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "accepted_extensions")
    private String acceptedExtensions;
    @Column(name = "url")
    private String url;
    @Column(name = "template_id")
    private Long templateId;
    @Column(name = "active")
    private Boolean active;
    @ManyToMany(mappedBy = "applications")
    private Set<UserGroupEntity> userGroup;
}
