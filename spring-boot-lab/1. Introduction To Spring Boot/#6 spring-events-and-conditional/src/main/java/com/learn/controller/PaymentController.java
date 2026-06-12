package com.learn.controller;

import com.learn.conditional.PaymentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Try:
 *   POST /api/payments/pay?amount=99.99
 *     → With app.payment.gateway=mock    → MockGateway responds
 *     → With app.payment.gateway=stripe  → StripeGateway responds
 *
 *   GET /api/payments/gateway
 *     → Shows which gateway bean is currently active
 *
 * Change app.payment.gateway in application.yml and restart to see the switch.
 * Spring injects whichever PaymentGateway bean satisfies the @ConditionalOnProperty.
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    // Spring injects whichever implementation is active — controller doesn't know which one
    private final PaymentGateway paymentGateway;

    @PostMapping("/pay")
    public ResponseEntity<Map<String, String>> pay(@RequestParam double amount) {
        String result = paymentGateway.pay(amount);
        return ResponseEntity.ok(Map.of(
                "gateway", paymentGateway.getGatewayName(),
                "result", result,
                "tip", "Change app.payment.gateway in application.yml and restart to switch gateway"
        ));
    }

    @GetMapping("/gateway")
    public ResponseEntity<Map<String, String>> getGateway() {
        return ResponseEntity.ok(Map.of(
                "activeGateway", paymentGateway.getGatewayName(),
                "tip", "This is @ConditionalOnProperty in action — set app.payment.gateway=stripe to switch"
        ));
    }
}
