package com.henheang.saasolv.ex05_chain;

/**
 * Mirrors saas-olv: LoggingInterceptor.
 * Logs the (already-sanitized, already-authenticated) request — runs LAST.
 */
public class LoggingInterceptor implements Interceptor {

    @Override
    public boolean preHandle(Request request) {
        System.out.println("[LOG] " + request.getPath() + " params=" + request.getParams());
        return true;
    }
}
