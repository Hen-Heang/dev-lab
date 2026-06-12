package com.learn.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simulates calling an external inventory service.
 *
 * Patterns demonstrated:
 *
 * @CircuitBreaker — after N failures, the circuit "opens" and calls go directly
 *   to the fallback without hitting the service. Recovers after a wait window.
 *
 * @Retry — retries failed calls up to N times before propagating the error.
 *   Applied before circuit breaker (inner decorator).
 *
 * @RateLimiter — limits to N calls per time window, returns fallback when exceeded.
 *
 * All config in application.yml under resilience4j.*
 */
@Slf4j
@Service
public class InventoryService {

    // Counts calls to simulate intermittent failures
    private final AtomicInteger callCount = new AtomicInteger(0);

    @CircuitBreaker(name = "inventoryService", fallbackMethod = "getStockFallback")
    @Retry(name = "inventoryService")
    public String getStock(Long productId) {
        int count = callCount.incrementAndGet();
        log.info("Calling inventory service (attempt #{})", count);

        // Simulate failure every 3rd call
        if (count % 3 == 0) {
            throw new RuntimeException("Inventory service is down!");
        }

        return "Product %d has 50 units in stock".formatted(productId);
    }

    @RateLimiter(name = "inventoryService", fallbackMethod = "getStockLimitedFallback")
    public String getStockRateLimited(Long productId) {
        return "Product %d stock (rate limited endpoint)".formatted(productId);
    }

    // Fallback for @CircuitBreaker — must have same return type + Throwable parameter
    public String getStockFallback(Long productId, Throwable ex) {
        log.warn("Circuit breaker fallback triggered for product {}: {}", productId, ex.getMessage());
        return "Fallback: inventory service unavailable. Cached stock for product " + productId + " is 10 units.";
    }

    // Fallback for @RateLimiter
    public String getStockLimitedFallback(Long productId, Throwable ex) {
        log.warn("Rate limiter fallback triggered for product {}", productId);
        return "Fallback: too many requests. Try again later.";
    }
}
