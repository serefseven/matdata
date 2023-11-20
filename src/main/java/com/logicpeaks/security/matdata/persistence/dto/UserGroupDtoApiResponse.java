package com.logicpeaks.security.matdata.persistence.dto;

import com.logicpeaks.security.matdata.enums.UserGroupStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupDtoApiResponse {
    private Long id;
    private String name;
    private Date endDate;
    private Boolean active;
    private List<Long> applicationIds;
}
