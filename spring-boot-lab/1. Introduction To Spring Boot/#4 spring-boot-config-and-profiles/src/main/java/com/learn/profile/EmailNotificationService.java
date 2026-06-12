package com.learn.profile;

import com.learn.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * @Profile("prod") — only active in production.
 * In PROD: we would send real emails via SMTP.
 * Here we simulate it using the email config from AppProperties.
 */
@Slf4j
@Service
@Profile("prod")
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {

    private final AppProperties appProperties;

    @Override
    public String send(String message) {
        String host = appProperties.getEmail().getHost();
        int port = appProperties.getEmail().getPort();
        log.info("[PROD - EMAIL] Sending via {}:{} — {}", host, port, message);
        return "[PROD] Email sent via %s:%d: %s".formatted(host, port, message);
    }

    @Override
    public String getType() {
        return "Email via SMTP (prod only)";
    }
}
