package com.logicpeaks.security.matdata.persistence.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDtoApiResponse {
    private Long id;
    private String name;
    private String type;
    private String fileName;
}
