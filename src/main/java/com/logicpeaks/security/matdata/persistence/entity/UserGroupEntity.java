package com.logicpeaks.security.matdata.persistence.entity;

import com.logicpeaks.security.matdata.enums.UserGroupStatus;
import com.logicpeaks.security.persistence.entity.PermissionEntity;
import com.logicpeaks.security.persistence.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user_group")
public class UserGroupEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_group_seq")
    @SequenceGenerator(name = "user_group_seq", sequenceName = "user_group_seq", allocationSize = 1)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "active")
    private Boolean active;
    @Column(name = "end_date")
    private Date endDate;

    @ManyToMany
    @JoinTable(
            name = "user_group_application",
            joinColumns = @JoinColumn(name = "user_group_id"),
            inverseJoinColumns = @JoinColumn(name = "application_id"))
    private Set<ApplicationEntity> applications;

    @OneToMany(mappedBy = "userGroup")
    private Set<UserEntity> users;
}
