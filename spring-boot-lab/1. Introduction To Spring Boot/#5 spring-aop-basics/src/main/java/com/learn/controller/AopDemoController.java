package com.learn.controller;

import com.learn.service.OrderService;
import com.learn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller to demonstrate all AOP scenarios.
 *
 * Run each endpoint and watch the LOGS — that's where AOP is visible.
 *
 * ┌─────────────────────────────────────────────────────────────────┐
 * │  ENDPOINT                         ASPECT THAT FIRES             │
 * ├─────────────────────────────────────────────────────────────────┤
 * │  GET  /api/aop/users              LoggingAspect (@Before+@After) │
 * │  GET  /api/aop/users/1            LoggingAspect (@Before+@After) │
 * │  GET  /api/aop/users/search?q=hen LoggingAspect + TimingAspect  │
 * │  GET  /api/aop/users/error        LoggingAspect (@AfterThrowing) │
 * │  GET  /api/aop/orders/1/status    LoggingAspect                  │
 * │  POST /api/aop/orders/1/place     LoggingAspect + TimingAspect  │
 * └─────────────────────────────────────────────────────────────────┘
 */
@RestController
@RequestMapping("/api/aop")
@RequiredArgsConstructor
public class AopDemoController {

    private final UserService userService;
    private final OrderService orderService;

    // ---- USER ENDPOINTS ----

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        List<String> users = userService.getAllUsers();
        return ResponseEntity.ok(Map.of(
                "users", users,
                "tip", "Check the logs — LoggingAspect fired @Before and @After on getAllUsers()"
        ));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable Long id) {
        String user = userService.getUser(id);
        return ResponseEntity.ok(Map.of(
                "id", id,
                "name", user,
                "tip", "Check the logs — @Before logged entry, @AfterReturning logged the result"
        ));
    }

    @GetMapping("/users/search")
    public ResponseEntity<Map<String, Object>> searchUsers(@RequestParam String q) throws InterruptedException {
        List<String> results = userService.slowSearch(q);
        return ResponseEntity.ok(Map.of(
                "query", q,
                "results", results,
                "tip", "Check the logs — TimingAspect (@Around) measured execution time of slowSearch()"
        ));
    }

    @GetMapping("/users/error")
    public ResponseEntity<Map<String, String>> triggerError() {
        try {
            userService.triggerError();
        } catch (RuntimeException ex) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", ex.getMessage(),
                    "tip", "Check the logs — @AfterThrowing fired in LoggingAspect"
            ));
        }
        return ResponseEntity.ok(Map.of("result", "no error"));
    }

    // ---- ORDER ENDPOINTS ----

    @GetMapping("/orders/{id}/status")
    public ResponseEntity<Map<String, Object>> getOrderStatus(@PathVariable Long id) {
        String status = orderService.getOrderStatus(id);
        return ResponseEntity.ok(Map.of(
                "orderId", id,
                "status", status,
                "tip", "LoggingAspect covers ALL service classes — not just UserService"
        ));
    }

    @PostMapping("/orders/{userId}/place")
    public ResponseEntity<Map<String, Object>> placeOrder(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "Laptop") String product) throws InterruptedException {
        String result = orderService.placeOrder(userId, product);
        return ResponseEntity.ok(Map.of(
                "result", result,
                "tip", "TimingAspect measured placeOrder() — it has @LogExecutionTime"
        ));
    }
}