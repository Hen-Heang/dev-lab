package com.learn.conditional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Active when app.payment.gateway=mock (or when property is missing — matchIfMissing=true).
 *
 * matchIfMissing = true means:
 *   If 'app.payment.gateway' is NOT set at all → use MockGateway as the default.
 *   This is a safe fallback for local development.
 *
 * Real-world pattern:
 *   In production:  app.payment.gateway=stripe  → StripeGateway activates
 *   In development: property not set OR =mock   → MockGateway activates (no real charges)
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "app.payment.gateway", havingValue = "mock", matchIfMissing = true)
public class MockGateway implements PaymentGateway {

    @Override
    public String pay(double amount) {
        log.info("[MOCK] Simulating payment of ${} (no real charge)", amount);
        return "[MOCK] Simulated payment of $%.2f — no real charge".formatted(amount);
    }

    @Override
    public String getGatewayName() {
        return "Mock Gateway (development only)";
    }
}
