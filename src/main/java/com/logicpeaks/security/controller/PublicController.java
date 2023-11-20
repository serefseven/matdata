package com.logicpeaks.security.controller;

import com.logicpeaks.security.persistence.dto.ResetPasswordRequest;
import com.logicpeaks.security.persistence.dto.UserDtoApiResponse;
import com.logicpeaks.security.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@AllArgsConstructor
public class PublicController {

    AccountService accountService;

    @PreAuthorize("permitAll()")
    @PostMapping("/reset-password")
    public ResponseEntity<Void> getAccountDetails(@RequestBody ResetPasswordRequest request) throws Exception {
        accountService.createResetPasswordToken(request);
        return ResponseEntity.ok().build();
    }

}
