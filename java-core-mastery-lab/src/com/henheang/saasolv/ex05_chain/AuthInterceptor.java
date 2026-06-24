package com.henheang.saasolv.ex05_chain;

/**
 * Mirrors saas-olv: AuthInterceptor.preHandle().
 * Blocks the request (returns false) if the user is not logged in.
 */
public class AuthInterceptor implements Interceptor {

    @Override
    public boolean preHandle(Request request) {
        if (!request.isLoggedIn()) {
            System.out.println("[AUTH] 401 blocked: " + request.getPath());
            return false; // stop the chain — request never reaches the controller
        }
        System.out.println("[AUTH] ok: " + request.getPath());
        return true;
    }
}
