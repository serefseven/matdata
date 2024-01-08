package com.logicpeaks.security.matdata.controller;

import com.logicpeaks.security.exception.DataNotFoundException;
import com.logicpeaks.security.matdata.persistence.dto.FileDtoApiResponse;
import com.logicpeaks.security.matdata.persistence.dto.InsertFileRequest;
import com.logicpeaks.security.matdata.persistence.dto.UpdateFileRequest;
import com.logicpeaks.security.matdata.persistence.dto.UpsertApplicationRequest;
import com.logicpeaks.security.matdata.persistence.entity.FileEntity;
import com.logicpeaks.security.matdata.service.ApplicationService;
import com.logicpeaks.security.matdata.service.FileService;
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
@RequestMapping("/file")
@AllArgsConstructor
public class FileController {

    private FileService fileService;


    @GetMapping
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.VIEW')")
    public List<FileDtoApiResponse> getList() {
        return fileService.getList();
    }

    @GetMapping("{fileId}")
    // TODO @PreAuthorize("hasAuthority('SECURITY.USER.VIEW')")
    public FileDtoApiResponse getFile(@PathVariable("fileId") Long fileId)
            throws DataNotFoundException {
        return fileService.getFileById(fileId);
    }

    @PostMapping
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.ADD')")
    public ResponseEntity<FileDtoApiResponse> createFile(@RequestParam("file") MultipartFile file,
                                                         @RequestParam("name") String name) throws IOException {
        FileDtoApiResponse fileDtoApiResponse = fileService
                .create(InsertFileRequest.builder()
                        .name(name)
                        .file(file)
                        .build());
        return ResponseEntity.status(HttpStatus.CREATED).body(fileDtoApiResponse);
    }

    @PutMapping
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.UPDATE')")
    public ResponseEntity<FileDtoApiResponse> updateApplication(@RequestBody UpdateFileRequest request) throws DataNotFoundException {
        FileDtoApiResponse fileDtoApiResponse = fileService.update(request);
        return ResponseEntity.status(HttpStatus.OK).body(fileDtoApiResponse);
    }

    @DeleteMapping("{fileId}")
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.DELETE')")
    public ResponseEntity deleteFile(@PathVariable Long fileId) {
        fileService.delete(fileId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/load/{fileId}")
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.ADD')")
    public ResponseEntity<Resource> processApplication(@PathVariable Long fileId) throws DataNotFoundException, IOException {
        FileEntity fileEntity = fileService.loadFile(fileId);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "filename="+fileEntity.getFileName());
        ByteArrayResource resource = new ByteArrayResource(fileEntity.getData());
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(fileEntity.getData().length)
                .contentType(MediaType.valueOf(fileEntity.getType()))
                .body(resource);
    }

}
