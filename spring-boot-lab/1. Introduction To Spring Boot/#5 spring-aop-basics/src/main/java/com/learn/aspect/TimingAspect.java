package com.learn.aspect;

import com.learn.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * ====================================================
 * TIMING ASPECT — @Around (most powerful advice)
 * ====================================================
 *
 * @Around wraps the entire method execution.
 * You control WHEN (and WHETHER) the real method runs by calling proceed().
 *
 * FLOW:
 *   1. TimingAspect.measure() starts
 *   2. Records start time
 *   3. Calls joinPoint.proceed() → the REAL method runs
 *   4. Records end time
 *   5. Logs duration
 *   6. Returns the result back to the caller
 *
 * This aspect intercepts methods annotated with @LogExecutionTime.
 * This is exactly how @Transactional works:
 *   - @Around begins a transaction before proceed()
 *   - If proceed() succeeds → commits
 *   - If proceed() throws → rolls back
 *
 * WARNING: If you forget to call joinPoint.proceed(), the real method never runs!
 */
@Slf4j
@Aspect
@Component
public class TimingAspect {

    /**
     * Intercepts any method annotated with @LogExecutionTime.
     * ProceedingJoinPoint is only available in @Around (not in @Before/@After).
     */
    @Around("@annotation(com.learn.annotation.LogExecutionTime)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        long start = System.currentTimeMillis();

        try {
            // This calls the REAL method — you must call this!
            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - start;
            log.info("[TIMING] {}.{}() executed in {}ms", className, methodName, duration);

            return result; // return the real method's result to the caller

        } catch (Throwable ex) {
            long duration = System.currentTimeMillis() - start;
            log.error("[TIMING] {}.{}() FAILED after {}ms — {}",
                    className, methodName, duration, ex.getMessage());
            throw ex; // re-throw so the caller sees the exception
        }
    }
}