package com.logicpeaks.security.matdata.service;

import com.logicpeaks.security.exception.DataNotFoundException;
import com.logicpeaks.security.matdata.persistence.dto.FileDtoApiResponse;
import com.logicpeaks.security.matdata.persistence.dto.InsertFileRequest;
import com.logicpeaks.security.matdata.persistence.dto.UpdateFileRequest;
import com.logicpeaks.security.matdata.persistence.entity.FileEntity;
import com.logicpeaks.security.matdata.persistence.repository.FileRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {

    private static final Logger log = LoggerFactory.getLogger(FileService.class);
    @NonNull
    private FileRepository fileRepository;
    @NonNull
    private final ModelMapper modelMapper;

    
    
    public List<FileDtoApiResponse> getList() {
        Sort sort = Sort.by(Sort.Direction.ASC,"id");

        List<FileEntity> list = fileRepository.findAll(sort);
        return list.stream().map(r -> modelMapper.map(r, FileDtoApiResponse.class)).collect(Collectors.toList());
    }

    public FileDtoApiResponse getFileById(Long Id) throws DataNotFoundException {

        Optional<FileEntity> file = fileRepository.findById(Id);
        if(!file.isPresent()){
            log.error(String.format("File not found"));
            throw new DataNotFoundException("File");
        }
        return modelMapper.map(file.get(), FileDtoApiResponse.class);
    }

    public FileDtoApiResponse create(InsertFileRequest request) throws IOException {

        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(request.getName());
        fileEntity.setData(request.getFile().getBytes());
        fileEntity.setType(request.getFile().getContentType());
        fileEntity.setFileName(request.getFile().getOriginalFilename());
        fileEntity = fileRepository.save(fileEntity);

        log.info(String.format("File create successful, data : %s",fileEntity));
        return modelMapper.map(fileEntity, FileDtoApiResponse.class);
    }

    public FileDtoApiResponse update(UpdateFileRequest request) throws DataNotFoundException {
        Optional<FileEntity> fileEntity = fileRepository.findById(request.getId());
        if(!fileEntity.isPresent()){
            log.error(String.format("File not found"));
            throw new DataNotFoundException("File");
        }
        fileEntity.get().setName(request.getName());
        FileEntity fileEntityResponse = fileRepository.save(fileEntity.get());
        log.info(String.format("File update successful, data :  %s",fileEntity));
        return modelMapper.map(fileEntityResponse, FileDtoApiResponse.class);
    }

    public void delete(Long id){
        fileRepository.deleteById(id);
        log.info(String.format("File delete successful, id : %s",id));
    }

    public FileEntity loadFile(Long Id) throws DataNotFoundException {

        Optional<FileEntity> file = fileRepository.findById(Id);
        if(!file.isPresent()){
            log.error(String.format("File not found"));
            throw new DataNotFoundException("File");
        }
        return file.get();
    }
}
