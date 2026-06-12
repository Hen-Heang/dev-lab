package com.learn.profile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * @Profile("dev") — Spring only creates this bean when the "dev" profile is active.
 *
 * Activate with:
 *   application.yml:           spring.profiles.active: dev
 *   Command line:              --spring.profiles.active=dev
 *   Environment variable:      SPRING_PROFILES_ACTIVE=dev
 *   @ActiveProfiles in tests:  @ActiveProfiles("dev")
 *
 * In DEV: we log to console (cheap, easy to see)
 */
@Slf4j
@Service
@Profile("dev")
public class ConsoleNotificationService implements NotificationService {

    @Override
    public String send(String message) {
        log.info("[DEV - CONSOLE] Notification: {}", message);
        return "[DEV] Logged to console: " + message;
    }

    @Override
    public String getType() {
        return "Console (dev only)";
    }
}
