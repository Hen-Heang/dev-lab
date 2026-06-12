package com.learn.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation — mark any method with this to automatically measure
 * and log its execution time via TimingAspect.
 *
 * Usage:
 *   @LogExecutionTime
 *   public UserResponse getUser(Long id) { ... }
 *
 * Result in logs:
 *   [TIMING] getUser executed in 42ms
 *
 * This is exactly how Spring's own @Transactional and @Cacheable work —
 * they are custom annotations that AOP intercepts.
 */
@Target(ElementType.METHOD)         // can only be placed on methods
@Retention(RetentionPolicy.RUNTIME) // must be available at runtime for Spring to read
public @interface LogExecutionTime {
}