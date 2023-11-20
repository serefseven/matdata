package com.logicpeaks.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

   @GetMapping
   public Map<String, Object> claims(@AuthenticationPrincipal JwtAuthenticationToken auth) {
        return auth.getTokenAttributes();
    }

    @GetMapping("/token")
    public String token(@AuthenticationPrincipal JwtAuthenticationToken auth) {
        return auth.getToken().getTokenValue();
    }

    @GetMapping("/role-owner")
    @PreAuthorize("hasPermission('account', 'VIEW')")
    public String role_owner() {
        return "OWNER";
    }

    @GetMapping("/role-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String role_admin() {
        return "ADMIN";
    }

    @GetMapping("/scope_messages_read")
    @PreAuthorize("hasAuthority('SCOPE_MESSAGES:READ')")
    public String scope_api_me_read() {
        return "You have 'MESSAGES:READ' scope";
    }
}
