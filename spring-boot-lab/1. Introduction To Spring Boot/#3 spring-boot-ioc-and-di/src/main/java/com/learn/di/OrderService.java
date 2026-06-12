package com.learn.di;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * ====================================================
 * THREE INJECTION STYLES — compared side by side
 * ====================================================
 *
 * 1. CONSTRUCTOR INJECTION (RECOMMENDED)
 *    - Dependencies are final (immutable)
 *    - Works without Spring (easier to unit test)
 *    - Makes dependencies explicit and required
 *    - @Autowired is optional when there's only one constructor
 *
 * 2. FIELD INJECTION (DISCOURAGED)
 *    - @Autowired directly on the field
 *    - Cannot make field final
 *    - Cannot test without Spring container
 *    - Hides dependencies (hard to see what a class needs)
 *
 * 3. SETTER INJECTION (for OPTIONAL dependencies)
 *    - Use when dependency is optional or can change
 *    - Less common, usually for legacy or optional config
 *
 * This class uses Constructor Injection (the right way).
 * @Qualifier picks a specific bean by name when multiple exist.
 */
@Slf4j
@Service
public class OrderService {

    // CONSTRUCTOR INJECTION — @Qualifier picks "stripe" specifically
    private final PaymentService paymentService;

    public OrderService(@Qualifier("stripe") PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public String placeOrder(String product, double price) {
        log.info("Placing order for {} at ${}", product, price);
        String paymentResult = paymentService.processPayment(price);
        return "Order placed: %s | %s".formatted(product, paymentResult);
    }
}
