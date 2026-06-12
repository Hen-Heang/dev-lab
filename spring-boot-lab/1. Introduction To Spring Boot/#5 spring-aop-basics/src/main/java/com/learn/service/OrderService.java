package com.learn.service;

import com.learn.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Another service — LoggingAspect covers ALL service classes automatically.
 * You don't need to touch LoggingAspect when you add new services.
 * That's the power of AOP — it scales across the whole codebase.
 */
@Slf4j
@Service
public class OrderService {

    @LogExecutionTime
    public String placeOrder(Long userId, String product) throws InterruptedException {
        // Simulate processing
        Thread.sleep(80);
        return "Order placed for user %d: %s (ref: ORD-%d)"
                .formatted(userId, product, System.currentTimeMillis() % 10000);
    }

    public String getOrderStatus(Long orderId) {
        return "Order %d is PROCESSING".formatted(orderId);
    }
}