package com.logicpeaks.security.matdata.persistence.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFileRequest {
    private Long id;
    private String name;
}
