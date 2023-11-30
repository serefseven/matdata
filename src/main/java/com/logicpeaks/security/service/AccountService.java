package com.logicpeaks.security.service;

import com.logicpeaks.security.exception.ArgumentNotValidException;
import com.logicpeaks.security.exception.DataNotFoundException;
import com.logicpeaks.security.enums.UserStatus;
import com.logicpeaks.security.persistence.dto.*;
import com.logicpeaks.security.persistence.entity.PasswordResetEntity;
import com.logicpeaks.security.persistence.entity.UserEntity;
import com.logicpeaks.security.persistence.repository.PasswordResetRepository;
import com.logicpeaks.security.persistence.repository.UserRepository;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    @NonNull
    private final EmailService emailService;

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

    private UserEntity getTokenUserEntity() throws DataNotFoundException {
        Map<String,Object> tokenAttributes = ((BearerTokenAuthentication)
                SecurityContextHolder.getContext().getAuthentication())
                .getTokenAttributes();

        if (!tokenAttributes.containsKey("user")) {
            return null;
        }

        String username = String.valueOf(tokenAttributes.get("user"));

        UserEntity userEntity = getUserByUserName(username);
        return userEntity;
    }

    public UserDtoApiResponse getAccountDetails() throws DataNotFoundException {

        UserEntity userEntity = getTokenUserEntity();

        UserDtoApiResponse userDtoApiResponse = modelMapper.map(userEntity,UserDtoApiResponse.class);
        if(userEntity.getUserGroup()!=null) {
            userDtoApiResponse.setUserGroupId(userEntity.getUserGroup().getId());
            userDtoApiResponse.setUserGroupName(userEntity.getUserGroup().getName());
        }
        return userDtoApiResponse;
    }

    public UserDtoApiResponse updateAccount(UpdateAccountRequest request) throws DataNotFoundException {

        UserEntity userEntity = getTokenUserEntity();
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());

        userRepository.save(userEntity);

        UserDtoApiResponse userDtoApiResponse = modelMapper.map(userEntity,UserDtoApiResponse.class);
        if(userEntity.getUserGroup()!=null) {
            userDtoApiResponse.setUserGroupId(userEntity.getUserGroup().getId());
            userDtoApiResponse.setUserGroupName(userEntity.getUserGroup().getName());
        }
        return userDtoApiResponse;
    }


    public UserDtoApiResponse changePassword(PasswordChangeRequest request) throws DataNotFoundException, ArgumentNotValidException {

        UserEntity userEntity = getTokenUserEntity();

        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();

        if (!bCrypt.matches(request.getCurrentPassword(), userEntity.getPassword())) {
            log.warn(String.format("Account update unsuccessful, current password incorrent."));
            throw new ArgumentNotValidException("Kullanılan Parola hatalı");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            log.warn(String.format("Account update unsuccessful, password missmatch error."));
            throw new ArgumentNotValidException("Parola ve parola doğrulama alanları eşleşmiyor.");
        }

        userEntity.setPassword(bCrypt.encode(request.getPassword()));

        userRepository.save(userEntity);

        UserDtoApiResponse userDtoApiResponse = modelMapper.map(userEntity,UserDtoApiResponse.class);
        if(userEntity.getUserGroup()!=null) {
            userDtoApiResponse.setUserGroupId(userEntity.getUserGroup().getId());
            userDtoApiResponse.setUserGroupName(userEntity.getUserGroup().getName());
        }
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

    public void createResetPasswordToken(ResetPasswordRequest request) throws DataNotFoundException, TemplateException, MessagingException, IOException {
        UserEntity userEntity = getUserByUserName(request.getUsername());

        UUID uuid = UUID.randomUUID();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        PasswordResetEntity passwordReset = new PasswordResetEntity();
        passwordReset.setUser(userEntity);
        passwordReset.setToken(uuid.toString());
        passwordReset.setExpireDate(calendar.getTime());
        passwordReset.setUsed(Boolean.FALSE);
        passwordResetRepository.save(passwordReset);

        emailService.sendPasswordResetEmail(userEntity.getEmail(),passwordReset.getToken());
    }

    public Boolean checkPasswordResetToken(CheckPasswordResetTokenRequest request) throws ArgumentNotValidException {

        Optional<PasswordResetEntity> tokenEntity = passwordResetRepository.findByToken(request.getToken());
        if(tokenEntity.isEmpty()){
            throw new ArgumentNotValidException("Token invalid");
        }

        int compareDate = tokenEntity.get().getExpireDate().compareTo(new Date());
        if(compareDate == -1 || tokenEntity.get().getUsed()){
            throw new ArgumentNotValidException("Token invalid");
        }

        return tokenEntity.get().getUser().getEmail().equals(request.getEmail());
    }

    public Boolean passwordReset(PasswordResetRequest request) throws ArgumentNotValidException {

        Optional<PasswordResetEntity> tokenEntity = passwordResetRepository.findByToken(request.getToken());
        if(tokenEntity.isEmpty()){
            throw new ArgumentNotValidException("Token invalid");
        }

        int compareDate = tokenEntity.get().getExpireDate().compareTo(new Date());
        if(compareDate == -1){
            throw new ArgumentNotValidException("Token invalid");
        }

        if (!tokenEntity.get().getUser().getEmail().equals(request.getEmail())){
            throw new ArgumentNotValidException("Token invalid");
        }

        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new ArgumentNotValidException("Mismatch password");
        }

        UserEntity userEntity = tokenEntity.get().getUser();
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        userEntity.setPassword(bCrypt.encode(request.getPassword()));
        userRepository.save(userEntity);

        tokenEntity.get().setUsed(Boolean.TRUE);
        passwordResetRepository.save(tokenEntity.get());

        return true;
    }
}
