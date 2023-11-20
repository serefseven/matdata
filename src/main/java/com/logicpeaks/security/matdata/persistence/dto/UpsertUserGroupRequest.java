package com.logicpeaks.security.matdata.persistence.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.logicpeaks.security.matdata.enums.UserGroupStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpsertUserGroupRequest {
    private Long id;

    @NotBlank(message = "Name must not be blank.")
    private String name;

    @Future(message = "End Date must not be future date.")
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date endDate;
    private Boolean active;

    private List<Long> applicationIds;
}
