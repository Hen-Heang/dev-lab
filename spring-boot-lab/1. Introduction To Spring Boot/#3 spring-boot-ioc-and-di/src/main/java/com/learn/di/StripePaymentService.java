package com.learn.di;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Second implementation of PaymentService.
 * Use @Qualifier("stripe") at the injection point to get this one specifically.
 */
@Slf4j
@Service("stripe")
public class StripePaymentService implements PaymentService {

    @Override
    public String processPayment(double amount) {
        log.info("Processing ${} via Stripe", amount);
        return "Payment of $%.2f processed via Stripe".formatted(amount);
    }

    @Override
    public String getProviderName() {
        return "Stripe";
    }
}
