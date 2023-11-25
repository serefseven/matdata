package com.logicpeaks.security.controller;

import com.logicpeaks.security.persistence.dto.CheckPasswordResetTokenRequest;
import com.logicpeaks.security.persistence.dto.PasswordResetRequest;
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

    @PostMapping("/password-reset")
    public ResponseEntity<Void> getAccountDetails(@RequestBody ResetPasswordRequest request) throws Exception {
        accountService.createResetPasswordToken(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check-password-reset-token")
    public ResponseEntity<Boolean> checkPasswordResetToken(@RequestBody CheckPasswordResetTokenRequest request) throws Exception {
        Boolean validity =  accountService.checkPasswordResetToken(request);
        return ResponseEntity.ok(validity);
    }

    @PostMapping("/set-password")
    public ResponseEntity<Boolean> passwordReset(@RequestBody PasswordResetRequest request) throws Exception {
        Boolean completion =  accountService.passwordReset(request);
        return ResponseEntity.ok(completion);
    }

}
