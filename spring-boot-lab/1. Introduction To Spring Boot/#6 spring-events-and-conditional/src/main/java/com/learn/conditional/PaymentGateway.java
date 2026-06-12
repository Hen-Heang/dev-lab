package com.learn.conditional;

/**
 * Contract for payment gateways.
 * Which implementation is active depends on app.payment.gateway in application.yml.
 */
public interface PaymentGateway {
    String pay(double amount);
    String getGatewayName();
}
