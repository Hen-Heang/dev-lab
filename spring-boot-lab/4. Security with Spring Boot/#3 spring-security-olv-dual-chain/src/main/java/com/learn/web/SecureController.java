package com.learn.web;

import java.security.Principal;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints that require authentication (anyRequest().authenticated()).
 *
 * In WEB mode: hitting /secure/me unauthenticated -> redirect to /login.
 * In API mode: hitting /api/me unauthenticated   -> 401 JSON.
 */
@RestController
public class SecureController {

    @GetMapping("/secure/me")
    public Map<String, Object> webMe(Principal principal) {
        return Map.of("user", principal.getName(), "scope", "secure-web");
    }

    @GetMapping("/api/me")
    public Map<String, Object> apiMe(Principal principal) {
        return Map.of("user", principal.getName(), "scope", "secure-api");
    }
}
