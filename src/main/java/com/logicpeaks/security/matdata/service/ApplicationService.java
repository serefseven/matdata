package com.logicpeaks.security.matdata.service;

import com.logicpeaks.security.exception.DataNotFoundException;
import com.logicpeaks.security.matdata.persistence.dto.ApplicationDtoApiResponse;
import com.logicpeaks.security.matdata.persistence.dto.UpsertApplicationRequest;
import com.logicpeaks.security.matdata.persistence.entity.ApplicationEntity;
import com.logicpeaks.security.matdata.persistence.entity.UserGroupEntity;
import com.logicpeaks.security.matdata.persistence.repository.ApplicationRepository;
import com.logicpeaks.security.matdata.persistence.repository.UserGroupRepository;
import com.logicpeaks.security.persistence.dto.UserDtoApiResponse;
import com.logicpeaks.security.service.AccountService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private static final Logger log = LoggerFactory.getLogger(ApplicationService.class);
    @NonNull
    private ApplicationRepository applicationRepository;
    @NonNull
    private final ModelMapper modelMapper;
    @NonNull
    private final AccountService accountService;
    @NonNull
    private final UserGroupRepository userGroupRepository;
    @NonNull
    private final RestTemplate restTemplate;

    @Value("${mat-data.application.url}")
    String matdataApplicationUrl;

    public List<ApplicationDtoApiResponse> getAccountApplicationList() throws DataNotFoundException {
        UserDtoApiResponse userDtoApiResponse = accountService.getAccountDetails();
        Optional<UserGroupEntity> userGroupEntity = userGroupRepository.findById(userDtoApiResponse.getUserGroupId());
        List<Long> applicationIds = userGroupEntity.get().getApplications().stream().map(i -> i.getId()).collect(Collectors.toList());
        List<ApplicationEntity> list = applicationRepository.findByIdInOrderByName(applicationIds);
        return list.stream().map(r -> modelMapper.map(r, ApplicationDtoApiResponse.class)).collect(Collectors.toList());
    }
    
    public List<ApplicationDtoApiResponse> getList() {
        Sort sort = Sort.by(Sort.Direction.ASC,"id");

        List<ApplicationEntity> list = applicationRepository.findAll(sort);
        return list.stream().map(r -> modelMapper.map(r, ApplicationDtoApiResponse.class)).collect(Collectors.toList());
    }

    public ApplicationDtoApiResponse getApplicationById(Long Id) throws DataNotFoundException {

        Optional<ApplicationEntity> app = applicationRepository.findById(Id);
        if(!app.isPresent()){
            log.error(String.format("Application not found"));
            throw new DataNotFoundException("Application");
        }
        return modelMapper.map(app.get(), ApplicationDtoApiResponse.class);
    }

    public ApplicationDtoApiResponse create(UpsertApplicationRequest request) {
        
        if(request.getActive()==null){
            request.setActive(true);
        }

        ApplicationEntity applicationEntity = modelMapper.map(request, ApplicationEntity.class);
        applicationEntity = applicationRepository.save(applicationEntity);

        log.info(String.format("Application create successful, data : %s",applicationEntity));
        return modelMapper.map(applicationEntity, ApplicationDtoApiResponse.class);
    }

    public ApplicationDtoApiResponse update(UpsertApplicationRequest request) {
        ApplicationEntity applicationEntity = modelMapper.map(request, ApplicationEntity.class);
        applicationEntity = applicationRepository.save(applicationEntity);
        log.info(String.format("Application update successful, data :  %s",applicationEntity));
        return modelMapper.map(applicationEntity, ApplicationDtoApiResponse.class);
    }

    public void delete(Long id){
        applicationRepository.deleteById(id);
        log.info(String.format("Application delete successful, id : %s",id));
    }

    public ResponseEntity<byte[]> sendToApp(Long applicationId, MultipartFile multipartFile) throws DataNotFoundException, IOException {
        Optional<ApplicationEntity> applicationEntity = applicationRepository.findById(applicationId);
        if(!applicationEntity.isPresent()){
            throw new DataNotFoundException("Application");
        }
        String url = matdataApplicationUrl+applicationEntity.get().getUrl();

        MultiValueMap<String,Object> multipartRequest = new LinkedMultiValueMap<>();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);//Main request's headers

        HttpHeaders requestHeadersAttachment = new HttpHeaders();
        requestHeadersAttachment.setContentType(MediaType.IMAGE_PNG);// extract mediatype from file extension
        HttpEntity<ByteArrayResource> attachmentPart;
        ByteArrayResource fileAsResource = new ByteArrayResource(multipartFile.getBytes()){
            @Override
            public String getFilename(){
                return multipartFile.getOriginalFilename();
            }
        };
        attachmentPart = new HttpEntity<>(fileAsResource,requestHeadersAttachment);

        multipartRequest.set("file",attachmentPart);

        HttpHeaders requestHeadersJSON = new HttpHeaders();
        requestHeadersJSON.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MultiValueMap<String,Object>> requestEntity = new HttpEntity<>(multipartRequest,requestHeaders);//final request

        ResponseEntity<byte[]> response = restTemplate.postForEntity(url,requestEntity,byte[].class);
        return response;
    }
}
