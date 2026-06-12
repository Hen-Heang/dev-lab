package com.learn.controller;

import com.learn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Try:
 *   POST /api/users/register?name=Heang&email=heang@test.com
 *     → Watch logs: UserService logs, then EmailListener, SmsListener, AuditListener
 *     → Notice: EmailListener and SmsListener run on different threads (async)
 *     → Notice: AuditListener runs on the SAME thread as UserService (sync)
 *
 *   GET /api/users → list all registered users
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @RequestParam String name,
            @RequestParam String email) {
        String result = userService.register(name, email);
        return ResponseEntity.ok(Map.of(
                "result", result,
                "tip", "Check the logs — 3 listeners fired automatically without UserService calling them"
        ));
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
