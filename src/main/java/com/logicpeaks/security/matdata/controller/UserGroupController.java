package com.logicpeaks.security.matdata.controller;

import com.logicpeaks.security.exception.DataNotFoundException;
import com.logicpeaks.security.matdata.persistence.dto.UserGroupDtoApiResponse;
import com.logicpeaks.security.matdata.persistence.dto.UpsertUserGroupRequest;
import com.logicpeaks.security.matdata.service.UserGroupService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usergroup")
@AllArgsConstructor
public class UserGroupController {

    private UserGroupService userGroupService;

    @GetMapping
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.VIEW')")
    public List<UserGroupDtoApiResponse> getList() {
        return userGroupService.getList();
    }

    @GetMapping("{userGroupId}")
    // TODO @PreAuthorize("hasAuthority('SECURITY.USER.VIEW')")
    public UserGroupDtoApiResponse getUser(@PathVariable("userGroupId") Long userGroupId)
            throws DataNotFoundException {
        return userGroupService.getUserGroupById(userGroupId);
    }

    @PostMapping
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.ADD')")
    public ResponseEntity<UserGroupDtoApiResponse> createUserGroup(@Valid @RequestBody UpsertUserGroupRequest upsertUserGroupRequest) {
        UserGroupDtoApiResponse userGroupDtoApiResponse = userGroupService.create(upsertUserGroupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userGroupDtoApiResponse);
    }

    @PutMapping
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.UPDATE')")
    public ResponseEntity<UserGroupDtoApiResponse> updateUserGroup(@Valid @RequestBody UpsertUserGroupRequest upsertUserGroupRequest) {
        UserGroupDtoApiResponse userGroupDtoApiResponse = userGroupService.update(upsertUserGroupRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userGroupDtoApiResponse);
    }

    @DeleteMapping("{userGroupId}")
    // TODO @PreAuthorize("hasAuthority('SECURITY.ROLE.DELETE')")
    public ResponseEntity deleteUserGroup(@PathVariable Long userGroupId) {
        userGroupService.delete(userGroupId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
