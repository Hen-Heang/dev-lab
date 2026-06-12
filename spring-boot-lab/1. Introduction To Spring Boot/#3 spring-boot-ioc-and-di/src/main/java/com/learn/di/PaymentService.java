package com.learn.di;

/**
 * Interface — the contract.
 * Using interfaces for injection allows Spring to swap implementations
 * without changing the consumer class. This is the power of IoC.
 */
public interface PaymentService {
    String processPayment(double amount);
    String getProviderName();
}
