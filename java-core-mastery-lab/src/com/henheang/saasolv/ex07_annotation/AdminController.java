package com.henheang.saasolv.ex07_annotation;

/**
 * A pretend controller. Some endpoints require a specific role, declared
 * DECLARATIVELY with @RequiresRole — exactly like @Token in olv-api.
 */
public class AdminController {

    @RequiresRole("ADMIN")
    public void deleteUser() {
        System.out.println("    >> user deleted");
    }

    @RequiresRole("MANAGER")
    public void approveLeave() {
        System.out.println("    >> leave approved");
    }

    // no annotation -> open to anyone
    public void viewDashboard() {
        System.out.println("    >> dashboard shown");
    }
}
