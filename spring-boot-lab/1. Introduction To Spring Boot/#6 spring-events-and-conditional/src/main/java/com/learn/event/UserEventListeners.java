package com.learn.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * ====================================================
 * EVENT LISTENERS — react to UserRegisteredEvent
 * ====================================================
 *
 * Each method here is completely independent.
 * None of them know about each other.
 * UserService knows about NONE of them.
 *
 * @EventListener — marks a method as an event handler.
 *   Spring calls it automatically when the matching event is published.
 *   The parameter type determines which event it handles.
 *
 * @Async — runs the listener on a SEPARATE thread (requires @EnableAsync on main class).
 *   Without @Async: all listeners run on the same thread as the publisher (blocking).
 *   With @Async: listeners run in background (non-blocking — good for email/SMS).
 *
 * EXECUTION ORDER (without @Async, same thread):
 *   1. UserService.register() publishes event
 *   2. sendWelcomeEmail() runs
 *   3. sendSmsNotification() runs
 *   4. writeAuditLog() runs
 *   5. UserService.register() continues
 *
 * EXECUTION ORDER (with @Async, separate threads):
 *   1. UserService.register() publishes event
 *   2. All 3 listeners start CONCURRENTLY on background threads
 *   3. UserService.register() continues IMMEDIATELY (doesn't wait)
 */
@Slf4j
@Component
public class UserEventListeners {

    /**
     * Listener 1 — Welcome email.
     * @Async runs on a background thread — doesn't block registration response.
     */
    @Async
    @EventListener
    public void sendWelcomeEmail(UserRegisteredEvent event) throws InterruptedException {
        Thread.sleep(100); // simulate email sending delay
        log.info("[EMAIL LISTENER] Sending welcome email to {} <{}>  | thread: {}",
                event.getName(), event.getEmail(), Thread.currentThread().getName());
    }

    /**
     * Listener 2 — SMS notification.
     * Completely decoupled from the email listener.
     */
    @Async
    @EventListener
    public void sendSmsNotification(UserRegisteredEvent event) throws InterruptedException {
        Thread.sleep(50); // simulate SMS sending delay
        log.info("[SMS LISTENER] Sending SMS to user {} | thread: {}",
                event.getName(), Thread.currentThread().getName());
    }

    /**
     * Listener 3 — Audit log.
     * Synchronous (no @Async) — runs on same thread as publisher.
     * Some operations need to stay in the same transaction.
     */
    @EventListener
    public void writeAuditLog(UserRegisteredEvent event) {
        log.info("[AUDIT LISTENER] New user registered — id={}, email={} | thread: {}",
                event.getUserId(), event.getEmail(), Thread.currentThread().getName());
    }
}
