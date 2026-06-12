package com.learn.di;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @Primary — when Spring sees multiple beans of the same type (PaymentService),
 * it injects this one by DEFAULT unless @Qualifier overrides it.
 *
 * Without @Primary or @Qualifier, Spring throws NoUniqueBeanDefinitionException.
 */
@Slf4j
@Service("paypal")
@Primary
public class PayPalPaymentService implements PaymentService {

    @Override
    public String processPayment(double amount) {
        log.info("Processing ${} via PayPal", amount);
        return "Payment of $%.2f processed via PayPal".formatted(amount);
    }

    @Override
    public String getProviderName() {
        return "PayPal";
    }
}
