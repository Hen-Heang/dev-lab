package com.henheang.saasolv.ex05_chain;

/**
 * Mirrors saas-olv: org.springframework.web.servlet.HandlerInterceptor (AuthInterceptor)
 * and the servlet Filter idea (XssFilter).
 *
 * preHandle returns true to continue the chain, false to STOP the request.
 * This is the Chain of Responsibility pattern.
 */
public interface Interceptor {

    boolean preHandle(Request request);
}
