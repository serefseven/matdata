package com.logicpeaks.security.controller;

import com.logicpeaks.security.persistence.dto.PasswordChangeRequest;
import com.logicpeaks.security.persistence.dto.TenantCheckDto;
import com.logicpeaks.security.persistence.dto.UpdateAccountRequest;
import com.logicpeaks.security.persistence.dto.UserDtoApiResponse;
import com.logicpeaks.security.service.AccountService;
import com.logicpeaks.security.service.TenantService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    AccountService accountService;

    @GetMapping
    public UserDtoApiResponse getAccountDetails() throws Exception {
        return accountService.getAccountDetails();
    }

    @PutMapping
    public UserDtoApiResponse updateAccount(@RequestBody UpdateAccountRequest request) throws Exception {
        return accountService.updateAccount(request);
    }
    @PutMapping("/change-password")
    public UserDtoApiResponse changePassword(@RequestBody PasswordChangeRequest request) throws Exception {
        return accountService.changePassword(request);
    }

    @GetMapping("/permissions")
    public List<String> getPermissions() throws Exception {
        return accountService.getPermission();
    }

    @GetMapping("/roles")
    public List<String> getRoles() throws Exception {
        return accountService.getRoles();
    }

}
