package com.logicpeaks.security.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@NamedEntityGraph(name = "RoleEntity.users", attributeNodes = @NamedAttributeNode("users"))
@NamedEntityGraph(name = "RoleEntity.permissions", attributeNodes = @NamedAttributeNode("permissions"))
@Table(name = "role")
public class RoleEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(name = "role_seq", sequenceName = "role_seq", allocationSize = 1)
    private Long id;
    @Column(name = "role")
    private String role;
    @Column(name = "description")
    private String description;
    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users;

    @ManyToMany
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<PermissionEntity> permissions;
}
