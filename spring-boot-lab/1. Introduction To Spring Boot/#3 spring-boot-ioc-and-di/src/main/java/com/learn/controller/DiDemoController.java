package com.learn.controller;

import com.learn.di.OrderService;
import com.learn.di.PaymentService;
import com.learn.scope.ScopeDemo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller to demonstrate DI concepts at runtime.
 *
 * Try these endpoints:
 *   GET  /api/di/default-payment    — uses @Primary bean (PayPal)
 *   GET  /api/di/order              — uses @Qualifier("stripe") (OrderService)
 *   GET  /api/di/scope              — demonstrates prototype scope (new instance each call)
 */
@RestController
@RequestMapping("/api/di")
@RequiredArgsConstructor
public class DiDemoController {

    // Injected via @Primary — Spring picks PayPal because it has @Primary
    private final PaymentService paymentService;

    // OrderService internally uses @Qualifier("stripe")
    private final OrderService orderService;

    // ApplicationContext lets us manually request beans — useful for scope demo
    private final ApplicationContext applicationContext;

    @GetMapping("/default-payment")
    public Map<String, String> defaultPayment(@RequestParam(defaultValue = "50.00") double amount) {
        return Map.of(
                "provider", paymentService.getProviderName(),
                "result", paymentService.processPayment(amount),
                "note", "PayPal was injected because it has @Primary"
        );
    }

    @GetMapping("/order")
    public Map<String, String> placeOrder(
            @RequestParam(defaultValue = "Laptop") String product,
            @RequestParam(defaultValue = "999.99") double price) {
        return Map.of(
                "result", orderService.placeOrder(product, price),
                "note", "OrderService uses @Qualifier(\"stripe\") — ignores @Primary"
        );
    }

    @GetMapping("/scope")
    public Map<String, String> scopeDemo() {
        // PROTOTYPE: each getBean() call returns a NEW instance
        ScopeDemo instance1 = applicationContext.getBean(ScopeDemo.class);
        ScopeDemo instance2 = applicationContext.getBean(ScopeDemo.class);

        return Map.of(
                "instance1", instance1.getInstanceId(),
                "instance2", instance2.getInstanceId(),
                "areSameObject", String.valueOf(instance1 == instance2),
                "note", "PROTOTYPE scope creates a new instance every time"
        );
    }
}
