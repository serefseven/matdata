package com.logicpeaks.security.controller;

import com.logicpeaks.security.exception.ArgumentNotValidException;
import com.logicpeaks.security.exception.DataNotFoundException;
import com.logicpeaks.security.persistence.dto.*;
import com.logicpeaks.security.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    UserService userService;

    @GetMapping
    // TODO @PreAuthorize("hasAuthority('SECURITY.USER.VIEW')")
    public List<UserDtoApiResponse> getList(@RequestParam int page,
                                            @RequestParam int size,
                                            @RequestParam(required = false) Optional<String> sort,
                                            @RequestParam(required = false) Optional<String> direction) {
        return userService.getList(page,size,direction,sort);
    }

    @GetMapping("{userId}")
    // TODO @PreAuthorize("hasAuthority('SECURITY.USER.VIEW')")
    public UserDtoApiResponse getUser(@PathVariable("userId") Long userId) throws DataNotFoundException {
        return userService.getUserById(userId);
    }

    @PostMapping
    // TODO @PreAuthorize("hasAuthority('SECURITY.USER.ADD')")
    public ResponseEntity<UserDtoApiResponse> createUser(@Valid @RequestBody CreateUserRequest createUserRequest)
            throws ArgumentNotValidException {
        UserDtoApiResponse userDtoApiResponse = userService.create(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDtoApiResponse);
    }

    @PutMapping
    // TODO @PreAuthorize("hasAuthority('SECURITY.USER.UPDATE')")
    public ResponseEntity<UserDtoApiResponse> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest)
            throws ArgumentNotValidException, DataNotFoundException {
        UserDtoApiResponse userDtoApiResponse = userService.update(updateUserRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userDtoApiResponse);
    }

    @DeleteMapping("{userId}")
    // TODO @PreAuthorize("hasAuthority('SECURITY.USER.DELETE')")
    public ResponseEntity deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/{userId}/role")
    // TODO @PreAuthorize("hasAuthority('SECURITY.USER.ROLE.VIEW')")
    public List<RoleDtoApiResponse> getRoleList(@PathVariable Long userId)
            throws DataNotFoundException {
        return userService.getRoleList(userId);
    }

    @PostMapping("/role")
    // TODO @PreAuthorize("hasAuthority('SECURITY.USER.ROLE.ADD')")
    public ResponseEntity assignRole(@RequestBody AssignRoleToUserRequest assignRoleToUserRequest)
            throws DataNotFoundException {
        userService.assignRole(assignRoleToUserRequest);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/{userId}/role/{roleId}")
    // TODO @PreAuthorize("hasAuthority('SECURITY.USER.ROLE.DELETE')")
    public ResponseEntity unassignRole(@PathVariable Long userId, @PathVariable Long roleId)
            throws DataNotFoundException {
        userService.unassignRole(userId, roleId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
