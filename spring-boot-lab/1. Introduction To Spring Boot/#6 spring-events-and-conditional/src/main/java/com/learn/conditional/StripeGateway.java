package com.learn.conditional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * ====================================================
 * @ConditionalOnProperty
 * ====================================================
 *
 * This bean is ONLY created when:
 *   app.payment.gateway=stripe   (in application.yml)
 *
 * If that property is missing or has a different value, this bean does NOT exist.
 *
 * This is exactly how Spring Boot auto-configuration works:
 *   - spring-boot-autoconfigure has hundreds of @ConditionalOnProperty checks
 *   - DataSourceAutoConfiguration only fires if you have a DB driver on classpath
 *   - WebMvcAutoConfiguration only fires if spring-webmvc is present
 *
 * Real use cases:
 *   - Switch between payment providers (Stripe vs PayPal vs KHQR)
 *   - Enable/disable features via config (feature flags)
 *   - Use mock implementations in dev, real ones in prod
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "app.payment.gateway", havingValue = "stripe")
public class StripeGateway implements PaymentGateway {

    @Override
    public String pay(double amount) {
        log.info("[STRIPE] Processing payment of ${}", amount);
        return "[STRIPE] Payment of $%.2f processed successfully".formatted(amount);
    }

    @Override
    public String getGatewayName() {
        return "Stripe (LIVE)";
    }
}
