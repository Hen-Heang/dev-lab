package com.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * ====================================================
 * SPRING EVENTS + @CONDITIONAL BEANS
 * ====================================================
 *
 * PART 1 — SPRING EVENTS (loose coupling between beans)
 * ─────────────────────────────────────────────────────
 * Problem without events:
 *   When a user registers, you need to: send email, send SMS, write audit log.
 *
 *   BAD — tight coupling:
 *     UserService → calls EmailService directly
 *     UserService → calls SmsService directly
 *     UserService → calls AuditService directly
 *     (UserService knows about all 3 — adding a 4th means editing UserService)
 *
 *   GOOD — with events:
 *     UserService → publishes UserRegisteredEvent (knows nothing else)
 *     EmailListener  → listens and sends email
 *     SmsListener    → listens and sends SMS
 *     AuditListener  → listens and writes audit log
 *     (Adding a 5th listener = zero changes to UserService)
 *
 * PART 2 — @CONDITIONAL BEANS (activate beans by config)
 * ────────────────────────────────────────────────────────
 * Spring Boot's entire auto-configuration is built on @Conditional.
 * Example: Spring only creates a DataSource bean IF you have a DB driver on the classpath.
 *
 * @ConditionalOnProperty — activate a bean only when a property equals a value.
 *
 *   app.payment.gateway=stripe  → StripeGateway bean is active
 *   app.payment.gateway=mock    → MockGateway bean is active
 *
 * TRY THESE ENDPOINTS:
 *   POST /api/users/register?name=Heang&email=heang@test.com → fires events
 *   GET  /api/users                                           → list users
 *   POST /api/payments/pay?amount=100                         → shows which gateway is active
 *   GET  /api/payments/gateway                                → shows active gateway name
 *
 * @EnableAsync — required for @Async event listeners to run on a separate thread
 */
@SpringBootApplication
@EnableAsync
public class SpringEventsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringEventsApplication.class, args);
    }
}
