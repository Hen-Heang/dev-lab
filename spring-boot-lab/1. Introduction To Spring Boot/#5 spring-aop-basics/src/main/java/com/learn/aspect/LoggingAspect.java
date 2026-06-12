package com.learn.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * ====================================================
 * LOGGING ASPECT
 * ====================================================
 *
 * This aspect automatically logs method entry/exit for every method
 * in the com.learn.service package — without touching the service classes.
 *
 * POINTCUT EXPRESSIONS:
 *
 *   execution(* com.learn.service.*.*(..))
 *   │          │  │                  │
 *   │          │  │                  └── any arguments
 *   │          │  └── any class in service package, any method name
 *   │          └── any return type
 *   └── match method execution
 *
 *   Other useful pointcut patterns:
 *     execution(* com.learn..*.*(..))           → all classes in com.learn and sub-packages
 *     execution(public * *(..))                 → all public methods anywhere
 *     @annotation(com.learn.annotation.LogExecutionTime) → methods with that annotation
 *     within(com.learn.controller.*)            → all methods in controller package
 *
 * ADVICE TYPES:
 *   @Before        — runs BEFORE the method
 *   @After         — runs AFTER the method (always, even if exception)
 *   @AfterReturning — runs AFTER method returns normally
 *   @AfterThrowing  — runs AFTER method throws exception
 *   @Around         — wraps the entire method (most powerful, see TimingAspect)
 */
@Slf4j
@Aspect
@Component
public class LoggingAspect {

    /**
     * Pointcut definition — reusable, referenced by name in advice below.
     * Matches ALL methods in ALL classes in the service package.
     */
    @Pointcut("execution(* com.learn.service.*.*(..))")
    public void serviceMethods() {}

    /**
     * @Before — runs before the matched method.
     * JoinPoint gives you access to: method name, arguments, target object.
     */
    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        log.info("[AOP BEFORE] {}.{}() called with args: {}",
                className, methodName, Arrays.toString(args));
    }

    /**
     * @AfterReturning — runs only when method returns normally (no exception).
     * 'returning = "result"' binds the return value to the parameter.
     */
    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.info("[AOP AFTER RETURNING] {}() returned: {}", methodName, result);
    }

    /**
     * @AfterThrowing — runs only when method throws an exception.
     * 'throwing = "ex"' binds the exception to the parameter.
     * Use for: centralized error logging, alerting.
     */
    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        log.error("[AOP AFTER THROWING] {}() threw: {} — {}",
                methodName, ex.getClass().getSimpleName(), ex.getMessage());
    }

    /**
     * @After — runs after method regardless of outcome (like finally {}).
     * Use for: cleanup, releasing resources.
     */
    @After("serviceMethods()")
    public void logAfter(JoinPoint joinPoint) {
        log.info("[AOP AFTER] {}() finished", joinPoint.getSignature().getName());
    }
}