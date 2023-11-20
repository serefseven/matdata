package com.logicpeaks.security.controller;

import com.logicpeaks.security.persistence.dto.TenantCheckDto;
import com.logicpeaks.security.persistence.dto.UserDtoApiResponse;
import com.logicpeaks.security.service.AccountService;
import com.logicpeaks.security.service.TenantService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/permissions")
    public List<String> getPermissions() throws Exception {
        return accountService.getPermission();
    }

    @GetMapping("/roles")
    public List<String> getRoles() throws Exception {
        return accountService.getRoles();
    }

}
