package com.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ====================================================
 * SPRING AOP — Aspect-Oriented Programming
 * ====================================================
 *
 * PROBLEM AOP SOLVES:
 *   Imagine every method in your app needs logging, timing, and security checks.
 *   Without AOP, you copy-paste that logic into every single method.
 *   With AOP, you write it ONCE in an Aspect — Spring applies it automatically.
 *
 * REAL-WORLD USE CASES:
 *   - Logging method entry/exit                    → LoggingAspect
 *   - Measuring execution time                     → TimingAspect
 *   - @Transactional (Spring uses AOP internally)
 *   - @PreAuthorize security checks (Spring Security uses AOP)
 *   - Caching (@Cacheable uses AOP)
 *
 * KEY TERMS:
 *   Aspect    = the class that holds your cross-cutting logic
 *   Advice    = WHAT to do (@Before, @After, @Around, @AfterThrowing)
 *   Pointcut  = WHERE to apply it (which methods/classes)
 *   JoinPoint = the actual method being intercepted at runtime
 *
 * HOW IT WORKS:
 *   Spring wraps your bean in a PROXY.
 *   When you call userService.getUser(), you're actually calling the PROXY.
 *   The proxy runs your aspect logic, then calls the real method.
 *
 * TRY THESE ENDPOINTS:
 *   GET  /api/aop/users/1          → LoggingAspect fires (@Before + @After)
 *   GET  /api/aop/users/slow       → TimingAspect fires (@Around)
 *   POST /api/aop/users            → LoggingAspect + validation fires
 *   GET  /api/aop/orders/1/place   → @Around advice wraps the method
 *   GET  /api/aop/users/error      → @AfterThrowing fires on exception
 */
@SpringBootApplication
public class SpringAopBasicsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringAopBasicsApplication.class, args);
    }
}