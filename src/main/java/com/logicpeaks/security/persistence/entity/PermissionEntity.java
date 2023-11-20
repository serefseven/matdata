package com.logicpeaks.security.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "permission")
public class PermissionEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permission_seq")
    @SequenceGenerator(name = "permission_seq", sequenceName = "permission_seq", allocationSize = 1)
    private Long id;
    @Column(name = "permission")
    private String permission;
    @Column(name = "description")
    private String description;
    @ManyToMany(mappedBy = "permissions")
    private Set<RoleEntity> roles;
}
