package com.logicpeaks.security.matdata.persistence.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDtoApiResponse {
    private Long id;
    private String name;
    private String description;
    private String acceptedExtensions;
    private String url;
    private Boolean active;
    private Long templateId;
}
