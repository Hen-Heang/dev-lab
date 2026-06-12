package com.learn.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Custom HealthIndicator — appears at /actuator/health as "externalService".
 * Spring Boot aggregates all HealthIndicator beans into the health endpoint.
 *
 * Simulates a check against an external service (e.g. a payment gateway).
 * In real usage: ping the service, check DB connection, verify disk space, etc.
 */
@Component("externalService")
public class ExternalServiceHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // Simulate: 80% chance the external service is up
        boolean isUp = ThreadLocalRandom.current().nextInt(10) < 8;

        if (isUp) {
            return Health.up()
                    .withDetail("service", "Payment Gateway")
                    .withDetail("responseTime", "12ms")
                    .build();
        } else {
            return Health.down()
                    .withDetail("service", "Payment Gateway")
                    .withDetail("error", "Connection timeout")
                    .build();
        }
    }
}
