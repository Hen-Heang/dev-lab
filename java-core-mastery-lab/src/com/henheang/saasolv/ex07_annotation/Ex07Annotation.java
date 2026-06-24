package com.henheang.saasolv.ex07_annotation;

import java.util.Set;

/**
 * EXERCISE 07 — Custom annotation + reflection enforcement.
 * Concept source: saas-olv @Token annotation read by ApiAccessInterceptor.
 */
public class Ex07Annotation {

    public static void main(String[] args) throws Exception {
        AdminController controller = new AdminController();
        RoleChecker checker = new RoleChecker();

        Set<String> userRoles = Set.of("MANAGER", "USER"); // current user's roles

        System.out.println("== user roles: " + userRoles + " ==");
        checker.invoke(controller, "viewDashboard", userRoles); // open
        checker.invoke(controller, "approveLeave", userRoles);  // MANAGER -> allowed
        checker.invoke(controller, "deleteUser", userRoles);    // ADMIN  -> denied

        /*
         * 🔧 PRACTICE IDEAS
         *  - Add @RequiresRole at CLASS level too, and have RoleChecker merge
         *    class-level + method-level requirements.
         *  - Allow multiple roles: String[] value() and pass if ANY matches.
         */
    }
}
