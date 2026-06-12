package com.learn.controller;

import com.learn.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     * Try calling this endpoint multiple times.
     * Every 3rd call simulates a failure.
     * After 5 failures the circuit opens and all calls go to fallback.
     * Monitor state at: GET /actuator/circuitbreakers
     */
    @GetMapping("/{productId}/stock")
    public String getStock(@PathVariable Long productId) {
        return inventoryService.getStock(productId);
    }

    /**
     * Rate limited to 5 calls per second (configured in application.yml).
     * Calls beyond the limit return the fallback response.
     */
    @GetMapping("/{productId}/stock/limited")
    public String getStockRateLimited(@PathVariable Long productId) {
        return inventoryService.getStockRateLimited(productId);
    }
}
