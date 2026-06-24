package com.henheang.saasolv.ex07_annotation;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Mirrors saas-olv: ApiAccessInterceptor reading @Token via reflection.
 *
 * This is meta-programming: at runtime we inspect a method, read its
 * @RequiresRole annotation, and enforce it BEFORE invoking — the same thing
 * Spring AOP / an interceptor does for you behind the scenes.
 */
public class RoleChecker {

    /**
     * Invoke a controller method only if the caller has the required role.
     *
     * @param target     the controller instance
     * @param methodName the method to call
     * @param userRoles  roles the current user holds
     */
    public void invoke(Object target, String methodName, Set<String> userRoles) throws Exception {
        Method method = target.getClass().getMethod(methodName);

        RequiresRole anno = method.getAnnotation(RequiresRole.class);
        if (anno != null) {
            String required = anno.value();
            if (!userRoles.contains(required)) {
                System.out.println("  DENIED " + methodName + " -> requires role " + required
                        + ", user has " + userRoles);
                return; // in Spring you'd throw AccessDeniedException here
            }
            System.out.println("  ALLOWED " + methodName + " (role " + required + " satisfied)");
        } else {
            System.out.println("  ALLOWED " + methodName + " (no role required)");
        }

        method.invoke(target); // reflectively call the real method
    }
}
