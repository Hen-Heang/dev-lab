package com.learn.web;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints open to everyone (matched by permitAll in both chains).
 */
@RestController
public class PublicController {

    @GetMapping("/public/ping")
    public Map<String, Object> publicPing() {
        return Map.of("ok", true, "scope", "public");
    }

    @GetMapping("/api/public/ping")
    public Map<String, Object> apiPublicPing() {
        return Map.of("ok", true, "scope", "api-public");
    }
}
