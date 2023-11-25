package com.logicpeaks.security.config.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;

import java.io.Serializable;
import java.util.Map;

public class CustomPermissionEvaluator implements PermissionEvaluator
{



    public CustomPermissionEvaluator(Object service) {
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object resource, Object scope) {
        Map<String,Object> tokenAttributes = ((BearerTokenAuthentication) authentication).getTokenAttributes();
        tokenAttributes.get("user");
        return true;// keycloakServiceClient.checkPermission(String.valueOf(resource),
                //Scope.valueOf(String.valueOf(scope)));
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
