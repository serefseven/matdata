package com.logicpeaks.security.matdata.controller;

import com.logicpeaks.security.exception.DataNotFoundException;
import com.logicpeaks.security.matdata.persistence.dto.ApplicationDtoApiResponse;
import com.logicpeaks.security.matdata.persistence.dto.UpsertApplicationRequest;
import com.logicpeaks.security.matdata.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/application")
@AllArgsConstructor
public class ApplicationController {

    private ApplicationService applicationService;

    @GetMapping("/account-apps")
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.VIEW')")
    public List<ApplicationDtoApiResponse> getAccountApplicationList() throws DataNotFoundException {
        return applicationService.getAccountApplicationList();
    }

    @GetMapping
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.VIEW')")
    public List<ApplicationDtoApiResponse> getList() {
        return applicationService.getList();
    }

    @GetMapping("{applicationId}")
    // TODO @PreAuthorize("hasAuthority('SECURITY.USER.VIEW')")
    public ApplicationDtoApiResponse getUser(@PathVariable("applicationId") Long applicationId)
            throws DataNotFoundException {
        return applicationService.getApplicationById(applicationId);
    }

    @PostMapping
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.ADD')")
    public ResponseEntity<ApplicationDtoApiResponse> createApplication(@Valid @RequestBody UpsertApplicationRequest upsertApplicationRequest) {
        ApplicationDtoApiResponse applicationDtoApiResponse = applicationService.create(upsertApplicationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationDtoApiResponse);
    }

    @PutMapping
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.UPDATE')")
    public ResponseEntity<ApplicationDtoApiResponse> updateApplication(@Valid @RequestBody UpsertApplicationRequest upsertApplicationRequest) {
        ApplicationDtoApiResponse applicationDtoApiResponse = applicationService.update(upsertApplicationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(applicationDtoApiResponse);
    }

    @DeleteMapping("{applicationId}")
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.DELETE')")
    public ResponseEntity deleteApplication(@PathVariable Long applicationId) {
        applicationService.delete(applicationId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/process/{applicationId}")
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.ADD')")
    public ResponseEntity<Resource> processApplication(@PathVariable Long applicationId,
                                                       @RequestParam("file") MultipartFile file) throws DataNotFoundException, IOException {
        ResponseEntity<byte[]> response = applicationService.sendToApp(applicationId,file);
        String contentDisposition = response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0);
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
        header.add("X-File-Name", contentDisposition);
        ByteArrayResource resource = new ByteArrayResource(response.getBody());
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(response.getBody().length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
