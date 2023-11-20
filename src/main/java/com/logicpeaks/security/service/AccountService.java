package com.logicpeaks.security.service;

import com.logicpeaks.security.exception.DataNotFoundException;
import com.logicpeaks.security.enums.UserStatus;
import com.logicpeaks.security.persistence.dto.ResetPasswordRequest;
import com.logicpeaks.security.persistence.dto.UserDtoApiResponse;
import com.logicpeaks.security.persistence.entity.PasswordResetEntity;
import com.logicpeaks.security.persistence.entity.UserEntity;
import com.logicpeaks.security.persistence.repository.PasswordResetRepository;
import com.logicpeaks.security.persistence.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);
    @Value("#{new Boolean('${security-service.properties.login-by-email}')}")
    private Boolean loginByEmail;
    @NonNull
    private UserRepository userRepository;
    @NonNull
    private PasswordResetRepository passwordResetRepository;
    @NonNull
    private final ModelMapper modelMapper;

    private UserEntity getUserByUserName(String username) throws DataNotFoundException {
        UserEntity userEntity;

        if(loginByEmail){
            userEntity = userRepository.findByEmailAndStatus(username, UserStatus.ACTIVE);
        } else {
            userEntity = userRepository.findByUsernameAndStatus(username, UserStatus.ACTIVE);
        }

        if(!Optional.ofNullable(userEntity).isPresent()) {
            log.error(String.format("User not found, username : %s", username));
            throw new DataNotFoundException("User");
        }
        return userEntity;
    }

    public UserDtoApiResponse getAccountDetails() throws DataNotFoundException {
        Map<String,Object> tokenAttributes = ((BearerTokenAuthentication)
                SecurityContextHolder.getContext().getAuthentication())
                .getTokenAttributes();

        if (!tokenAttributes.containsKey("user")) {
            return null;
        }

        String username = String.valueOf(tokenAttributes.get("user"));

        UserEntity userEntity = getUserByUserName(username);

        UserDtoApiResponse userDtoApiResponse = modelMapper.map(userEntity,UserDtoApiResponse.class);
        if(userEntity.getUserGroup()!=null)
            userDtoApiResponse.setUserGroupId(userEntity.getUserGroup().getId());
        return userDtoApiResponse;
    }

    public List<String> getPermission() throws DataNotFoundException {
        Map<String,Object> tokenAttributes = ((BearerTokenAuthentication)
                SecurityContextHolder.getContext().getAuthentication())
                .getTokenAttributes();

        if (!tokenAttributes.containsKey("user")) {
            return new ArrayList<>();
        }

        String username = String.valueOf(tokenAttributes.get("user"));

        UserEntity userEntity;

        if(loginByEmail){
            userEntity = userRepository.findByEmailAndStatus(username, UserStatus.ACTIVE);
        } else {
            userEntity = userRepository.findByUsernameAndStatus(username, UserStatus.ACTIVE);
        }

        if(!Optional.ofNullable(userEntity).isPresent()) {
            log.error(String.format("User not found, username : %s", username));
            throw new DataNotFoundException("User");
        }

        return userEntity
                .getRoles()
                .stream()
                .flatMap(r -> r.getPermissions().
                        stream()).
                map(p -> p.getPermission())
                .collect(Collectors.toList());
    }

    public List<String> getRoles() throws DataNotFoundException {
        Map<String,Object> tokenAttributes = ((BearerTokenAuthentication)
                SecurityContextHolder.getContext().getAuthentication())
                .getTokenAttributes();

        if (!tokenAttributes.containsKey("user")) {
            return new ArrayList<>();
        }

        String username = String.valueOf(tokenAttributes.get("user"));

        UserEntity userEntity;

        if(loginByEmail){
            userEntity = userRepository.findByEmailAndStatus(username, UserStatus.ACTIVE);
        } else {
            userEntity = userRepository.findByUsernameAndStatus(username, UserStatus.ACTIVE);
        }

        if(!Optional.ofNullable(userEntity).isPresent()) {
            log.error(String.format("User not found, username : %s", username));
            throw new DataNotFoundException("User");
        }

        return userEntity
                .getRoles()
                .stream()
                .map(p -> p.getRole())
                .collect(Collectors.toList());
    }

    public void createResetPasswordToken(ResetPasswordRequest request) throws DataNotFoundException {
        UserEntity userEntity = getUserByUserName(request.getUsername());

        UUID uuid = UUID.randomUUID();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        PasswordResetEntity passwordReset = new PasswordResetEntity();
        passwordReset.setUser(userEntity);
        passwordReset.setToken(uuid.toString());
        passwordReset.setExpireDate(calendar.getTime());
        passwordResetRepository.save(passwordReset);
    }
}
