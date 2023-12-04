package com.logicpeaks.security.matdata.persistence.dto;

import com.logicpeaks.security.matdata.persistence.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpsertApplicationRequest {
    private Long id;

    @NotBlank(message = "Name must not be blank.")
    private String name;
    private String description;

    @NotBlank(message = "Accepted extension must not be blank.")
    private String acceptedExtensions;

    @NotBlank(message = "URL must not be blank.")
    private String url;
    private Boolean active;
    private Long templateId;
    private Long logoId;
}
