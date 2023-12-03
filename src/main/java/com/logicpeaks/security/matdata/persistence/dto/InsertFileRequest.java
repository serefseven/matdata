package com.logicpeaks.security.matdata.persistence.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class InsertFileRequest {

    private Long id;
    private String name;
    private MultipartFile file;

    public InsertFileRequest(Long id, String name, MultipartFile file) {
        this.id = id;
        this.name = name;
        this.file = file;
    }
}
