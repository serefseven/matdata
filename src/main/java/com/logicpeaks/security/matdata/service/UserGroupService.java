package com.logicpeaks.security.matdata.service;

import com.logicpeaks.security.exception.DataNotFoundException;
import com.logicpeaks.security.matdata.enums.UserGroupStatus;
import com.logicpeaks.security.matdata.persistence.dto.UserGroupDtoApiResponse;
import com.logicpeaks.security.matdata.persistence.dto.UpsertUserGroupRequest;
import com.logicpeaks.security.matdata.persistence.entity.ApplicationEntity;
import com.logicpeaks.security.matdata.persistence.entity.UserGroupEntity;
import com.logicpeaks.security.matdata.persistence.repository.ApplicationRepository;
import com.logicpeaks.security.matdata.persistence.repository.UserGroupRepository;
import com.logicpeaks.security.matdata.persistence.repository.UserGroupRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserGroupService {

    private static final Logger log = LoggerFactory.getLogger(UserGroupService.class);
    @NonNull
    private UserGroupRepository userGroupRepository;
    @NonNull
    private ApplicationRepository applicationRepository;
    @NonNull
    private final ModelMapper modelMapper;
    
    public List<UserGroupDtoApiResponse> getList() {
        Sort sort = Sort.by(Sort.Direction.ASC,"id");

        List<UserGroupEntity> list = userGroupRepository.findAll(sort);
        return list.stream().map(r -> modelMapper.map(r, UserGroupDtoApiResponse.class)).collect(Collectors.toList());
    }

    public UserGroupDtoApiResponse getUserGroupById(Long Id) throws DataNotFoundException {

        Optional<UserGroupEntity> app = userGroupRepository.findById(Id);
        if(!app.isPresent()){
            log.error(String.format("UserGroup not found"));
            throw new DataNotFoundException("UserGroup");
        }
        UserGroupDtoApiResponse response = modelMapper.map(app.get(), UserGroupDtoApiResponse.class);
        response.setApplicationIds(app.get().getApplications().stream().map(i -> i.getId()).collect(Collectors.toList()));
        return response;
    }

    public UserGroupDtoApiResponse create(UpsertUserGroupRequest request) {
        
        if(request.getActive()==null){
            request.setActive(Boolean.TRUE);
        }

        List<ApplicationEntity> applicationEntityList = new ArrayList<>();
        if(request.getApplicationIds() != null && request.getApplicationIds().size()>0){
            applicationEntityList = applicationRepository.findAllById(request.getApplicationIds());
        }

        UserGroupEntity userGroupEntity = modelMapper.map(request, UserGroupEntity.class);
        userGroupEntity.setApplications(applicationEntityList.stream().collect(Collectors.toSet()));
        userGroupEntity = userGroupRepository.save(userGroupEntity);

        log.info(String.format("UserGroup create successful, data : %s",userGroupEntity));
        return modelMapper.map(userGroupEntity, UserGroupDtoApiResponse.class);
    }

    public UserGroupDtoApiResponse update(UpsertUserGroupRequest request) {
        UserGroupEntity userGroupEntity = modelMapper.map(request, UserGroupEntity.class);

        List<ApplicationEntity> applicationEntityList = new ArrayList<>();
        if(request.getApplicationIds() != null && request.getApplicationIds().size()>0){
            applicationEntityList = applicationRepository.findAllById(request.getApplicationIds());
        }
        userGroupEntity.setApplications(applicationEntityList.stream().collect(Collectors.toSet()));
        userGroupEntity = userGroupRepository.save(userGroupEntity);
        log.info(String.format("UserGroup update successful, data :  %s",userGroupEntity));
        return modelMapper.map(userGroupEntity, UserGroupDtoApiResponse.class);
    }

    public void delete(Long id){
        userGroupRepository.deleteById(id);
        log.info(String.format("UserGroup delete successful, id : %s",id));
    }
}
