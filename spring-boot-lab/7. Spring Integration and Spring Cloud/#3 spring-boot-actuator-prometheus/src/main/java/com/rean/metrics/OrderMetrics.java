package com.learn.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Custom Micrometer metrics using MeterRegistry.
 *
 * Counter  — tracks how many times something happened (e.g. orders placed)
 * Timer    — tracks duration + count of operations (e.g. order processing time)
 *
 * These metrics are automatically exposed at /actuator/prometheus
 * and can be scraped by Prometheus and visualized in Grafana.
 */
@Component
public class OrderMetrics {

    private final Counter ordersPlacedCounter;
    private final Counter ordersCancelledCounter;
    private final Timer orderProcessingTimer;

    public OrderMetrics(MeterRegistry registry) {
        this.ordersPlacedCounter = Counter.builder("orders.placed")
                .description("Total number of orders placed")
                .tag("app", "spring-boot-actuator-prometheus")
                .register(registry);

        this.ordersCancelledCounter = Counter.builder("orders.cancelled")
                .description("Total number of orders cancelled")
                .tag("app", "spring-boot-actuator-prometheus")
                .register(registry);

        this.orderProcessingTimer = Timer.builder("orders.processing.time")
                .description("Time taken to process an order")
                .tag("app", "spring-boot-actuator-prometheus")
                .register(registry);
    }

    public void incrementOrdersPlaced() {
        ordersPlacedCounter.increment();
    }

    public void incrementOrdersCancelled() {
        ordersCancelledCounter.increment();
    }

    public void recordOrderProcessingTime(long milliseconds) {
        orderProcessingTimer.record(milliseconds, TimeUnit.MILLISECONDS);
    }
}
