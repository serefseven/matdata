package com.logicpeaks.security.persistence.dto;

import com.logicpeaks.security.enums.UserStatus;
import com.logicpeaks.security.enums.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDtoApiResponse {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private UserStatus status;
    private UserType type;
    private Long userGroupId;
    private String userGroupName;

}
