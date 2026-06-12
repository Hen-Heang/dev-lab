package com.learn.controller;

import com.learn.config.AppProperties;
import com.learn.profile.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * Demonstrates both @Value and @ConfigurationProperties side by side.
 *
 * Try:
 *   GET /api/config/info          — see app config
 *   GET /api/config/profiles      — see active profiles
 *   POST /api/config/notify       — see which NotificationService is active
 */
@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class ConfigDemoController {

    // @Value — simple, direct, one property at a time
    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    // @ConfigurationProperties — type-safe, grouped, recommended for multiple props
    private final AppProperties appProperties;

    // NotificationService — which implementation is injected depends on active profile
    private final NotificationService notificationService;

    // Environment — lets you inspect the Spring environment programmatically
    private final Environment environment;

    @GetMapping("/info")
    public Map<String, Object> info() {
        return Map.of(
                "nameViaValue", appName,              // from @Value
                "versionViaValue", appVersion,        // from @Value
                "nameViaProps", appProperties.getName(),  // from @ConfigurationProperties
                "maxUploadMb", appProperties.getMaxUploadSizeMb(),
                "emailHost", appProperties.getEmail().getHost(),
                "emailSsl", appProperties.getEmail().isSsl()
        );
    }

    @GetMapping("/profiles")
    public Map<String, Object> profiles() {
        return Map.of(
                "activeProfiles", Arrays.asList(environment.getActiveProfiles()),
                "defaultProfiles", Arrays.asList(environment.getDefaultProfiles()),
                "notificationServiceType", notificationService.getType()
        );
    }

    @PostMapping("/notify")
    public Map<String, String> notify(@RequestBody String message) {
        return Map.of(
                "activeProfile", Arrays.toString(environment.getActiveProfiles()),
                "serviceUsed", notificationService.getType(),
                "result", notificationService.send(message)
        );
    }
}
