package com.logicpeaks.security.persistence.entity;

import com.logicpeaks.security.enums.UserStatus;
import com.logicpeaks.security.matdata.persistence.entity.UserGroupEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "password_reset")
public class PasswordResetEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_reset_seq")
    @SequenceGenerator(name = "password_reset_seq", sequenceName = "password_reset_seq", allocationSize = 1)
    private Long id;
    @Column(name = "token")
    private String token;
    @Column(name = "expire_date")
    private Date expireDate;
    @Column(name = "used")
    private Boolean used;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable=false)
    private UserEntity user;

}
