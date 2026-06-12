package com.learn.service;

import com.learn.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * UserService — publishes events but knows NOTHING about listeners.
 *
 * ApplicationEventPublisher is the only thing needed to publish events.
 * Spring injects it automatically — no extra config needed.
 *
 * Notice what UserService does NOT import:
 *   ❌ EmailService
 *   ❌ SmsService
 *   ❌ AuditService
 *
 * It only knows: "a user was registered — I'll announce it. Who reacts is not my problem."
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final ApplicationEventPublisher eventPublisher;

    private final List<String> registeredUsers = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public String register(String name, String email) {
        Long userId = idCounter.getAndIncrement();
        registeredUsers.add("%d:%s:%s".formatted(userId, name, email));

        log.info("[USER SERVICE] User registered: id={}, name={}", userId, name);

        // Publish event — all listeners will react automatically
        // UserService does not call any listener directly
        eventPublisher.publishEvent(new UserRegisteredEvent(this, userId, name, email));

        log.info("[USER SERVICE] Event published — registration complete");
        return "User registered successfully: id=%d, name=%s".formatted(userId, name);
    }

    public List<String> getAllUsers() {
        return List.copyOf(registeredUsers);
    }
}
