package com.logicpeaks.security.service;

import com.logicpeaks.security.exception.DataNotFoundException;
import com.logicpeaks.security.exception.ArgumentNotValidException;
import com.logicpeaks.security.enums.UserStatus;
import com.logicpeaks.security.matdata.persistence.entity.UserGroupEntity;
import com.logicpeaks.security.matdata.persistence.repository.UserGroupRepository;
import com.logicpeaks.security.persistence.dto.*;
import com.logicpeaks.security.persistence.entity.RoleEntity;
import com.logicpeaks.security.persistence.entity.UserEntity;
import com.logicpeaks.security.persistence.repository.RoleRepository;
import com.logicpeaks.security.persistence.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Value("#{new Boolean('${security-service.properties.login-by-email}')}")
    private Boolean loginByEmail;
    @NonNull
    private UserRepository userRepository;
    @NonNull
    private UserGroupRepository userGroupRepository;
    @NonNull
    private RoleRepository roleRepository;
    @NonNull
    private final ModelMapper modelMapper;

    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserEntity userEntity;


        if(loginByEmail){
            userEntity = userRepository.findByEmailAndStatus(username, UserStatus.ACTIVE);
            if(!Optional.ofNullable(userEntity).isPresent())
                throw new UsernameNotFoundException(username);
            return User
                    .withUsername(userEntity.getEmail())
                    .password(userEntity.getPassword())
                    .authorities("read")
                    .build();
        } else {
            userEntity = userRepository.findByUsernameAndStatus(username, UserStatus.ACTIVE);
            if(!Optional.ofNullable(userEntity).isPresent())
                throw new UsernameNotFoundException(username);
            return User
                    .withUsername(userEntity.getUsername())
                    .password(userEntity.getPassword())
                    .authorities("read")
                    .build();
        }

    }

    public List<UserDtoApiResponse> getList(int page,
                                            int size,
                                            Optional<String> sortDirection,
                                            Optional<String> sortField) {

        Sort sort = Sort.by(Sort.Direction.valueOf(sortDirection.orElse("DESC")), sortField.orElse("id"));
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<UserEntity> userList = userRepository.findAll(pageable);
        return userList.stream().map(r -> modelMapper.map(r, UserDtoApiResponse.class)).collect(Collectors.toList());
    }

    public UserDtoApiResponse getUserById(Long userId) throws DataNotFoundException {

        Optional<UserEntity> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            log.error(String.format("User not found"));
            throw new DataNotFoundException("User");
        }

        UserDtoApiResponse userDtoApiResponse = modelMapper.map(user.get(), UserDtoApiResponse.class);
        if (user.get().getUserGroup() != null)
            userDtoApiResponse.setUserGroupId(user.get().getUserGroup().getId());
        return userDtoApiResponse;
    }

    public UserDtoApiResponse create(CreateUserRequest request)
            throws ArgumentNotValidException {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            log.warn(String.format("User update unsuccessful, password missmatch error."));
            throw new ArgumentNotValidException("Parola ve parola doğrulama alanları eşleşmiyor.");
        }

        Optional<UserEntity> userEntityEmailCheck = userRepository.findByEmail(request.getEmail());
        if (userEntityEmailCheck.isPresent()) {
            log.warn(String.format("User create unsuccessful, e-mail already exist."));
            throw new ArgumentNotValidException("E-mail adresi zaten kayıtlı.");
        }

        if (request.getStatus() == null) {
            request.setStatus(UserStatus.ACTIVE);
        }
        UserGroupEntity userGroupEntity = null;
        if (request.getUserGroupId() != null) {
            userGroupEntity = userGroupRepository.findById(request.getUserGroupId()).orElse(null);
        }

        UserEntity userEntity = modelMapper.map(request, UserEntity.class);
        userEntity.setUserGroup(userGroupEntity);
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        userEntity.setPassword(bCrypt.encode(userEntity.getPassword()));
        userEntity = userRepository.save(userEntity);

        log.info(String.format("User create successful, data : %s", userEntity));
        return modelMapper.map(userEntity, UserDtoApiResponse.class);
    }

    public UserDtoApiResponse update(UpdateUserRequest request)
            throws ArgumentNotValidException, DataNotFoundException {

        Optional<UserEntity> userEntityOptional = userRepository.findById(request.getId());
        UserEntity userEntity = userEntityOptional.orElseThrow(() -> new DataNotFoundException("User"));

        if (Optional.ofNullable(request.getPassword()).isPresent()) {
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                log.warn(String.format("User update unsuccessful, password missmatch error."));
                throw new ArgumentNotValidException("Parola ve parola doğrulama alanları eşleşmiyor.");
            }
            BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
            userEntity.setPassword(bCrypt.encode(request.getPassword()));
        }

        if (Optional.ofNullable(request.getEmail()).isPresent()) {
            Optional<UserEntity> userEntityEmailCheck = userRepository.findByEmail(request.getEmail());
            if (userEntityEmailCheck.isPresent() && !userEntityEmailCheck.get().getId().equals(request.getId())) {
                log.warn(String.format("User update unsuccessful, e-mail already exist."));
                throw new ArgumentNotValidException("E-mail adresi zaten kayıtlı.");
            }
            userEntity.setEmail(request.getEmail());
        }

        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        userEntity.setStatus(request.getStatus());
        userEntity.setType(request.getType());


        UserGroupEntity userGroupEntity = null;
        if (request.getUserGroupId() != null) {
            userGroupEntity = userGroupRepository.findById(request.getUserGroupId()).orElse(null);
        }
        userEntity.setUserGroup(userGroupEntity);
        userEntity = userRepository.save(userEntity);
        log.info(String.format("User update successful, data :  %s", userEntity));
        return modelMapper.map(userEntity, UserDtoApiResponse.class);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info(String.format("User delete successful, id : %s", id));
    }

    public List<RoleDtoApiResponse> getRoleList(Long id)
            throws DataNotFoundException {
        Optional<UserEntity> userEntityOptional = userRepository.findById(id);

        if (userEntityOptional.isEmpty()) {
            log.error(String.format("User not found"));
            throw new DataNotFoundException("User");
        }

        return userEntityOptional.get()
                .getRoles()
                .stream().map(r -> modelMapper.map(r, RoleDtoApiResponse.class))
                .collect(Collectors.toList());
    }

    public void assignRole(AssignRoleToUserRequest request)
            throws DataNotFoundException {
        Optional<UserEntity> userEntityOptional = userRepository.findById(request.getUserId());
        Optional<RoleEntity> roleEntityOptional = roleRepository.findById(request.getRoleId());

        if (userEntityOptional.isEmpty()) {
            log.error(String.format("User not found"));
            throw new DataNotFoundException("User");
        }

        if (roleEntityOptional.isEmpty()) {
            log.error(String.format("Role not found"));
            throw new DataNotFoundException("Role");
        }

        if (Optional.ofNullable(userEntityOptional.get().getRoles()).isEmpty()) {
            userEntityOptional.get().setRoles(new HashSet<>());
        }

        userEntityOptional.get().getRoles().add(roleEntityOptional.get());
        userRepository.save(userEntityOptional.get());
    }

    public void unassignRole(Long userId, Long roleId)
            throws DataNotFoundException {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);

        if (userEntityOptional.isEmpty()) {
            log.error(String.format("User not found"));
            throw new DataNotFoundException("User");
        }

        if (Optional.ofNullable(userEntityOptional.get().getRoles()).isEmpty()) {
            userEntityOptional.get().setRoles(new HashSet<>());
        }

        Boolean roleExist = userEntityOptional.get().getRoles().stream().anyMatch(r -> r.getId().equals(roleId));
        if (!roleExist) {
            log.error(String.format("Role not found"));
            throw new DataNotFoundException("Role");
        }

        Set<RoleEntity> newRoleList = userEntityOptional.get()
                .getRoles()
                .stream()
                .filter(r -> !r.getId().equals(roleId))
                .collect(Collectors.toSet());

        userEntityOptional.get().setRoles(newRoleList);

        userRepository.save(userEntityOptional.get());
    }
}
