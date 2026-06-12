package com.learn.controller;

import com.learn.metrics.OrderMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderMetrics orderMetrics;

    @PostMapping
    public String placeOrder(@RequestBody String orderDetails) {
        long start = System.currentTimeMillis();

        // Simulate processing
        log.info("Processing order: {}", orderDetails);
        orderMetrics.incrementOrdersPlaced();

        long elapsed = System.currentTimeMillis() - start;
        orderMetrics.recordOrderProcessingTime(elapsed);

        return "Order placed successfully";
    }

    @DeleteMapping("/{id}")
    public String cancelOrder(@PathVariable Long id) {
        log.info("Cancelling order: {}", id);
        orderMetrics.incrementOrdersCancelled();
        return "Order " + id + " cancelled";
    }
}
