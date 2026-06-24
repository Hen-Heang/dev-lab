package com.henheang.saasolv.ex05_chain;

import java.util.Map;

/**
 * Mirrors saas-olv: XssFilter / XssRequestWrapper.
 * Sanitizes every parameter (strips angle brackets) — runs FIRST.
 * Never stops the request; it just cleans it (returns true).
 */
public class XssInterceptor implements Interceptor {

    @Override
    public boolean preHandle(Request request) {
        for (Map.Entry<String, String> e : request.getParams().entrySet()) {
            String cleaned = e.getValue()
                    .replace("<", "&lt;")
                    .replace(">", "&gt;");
            if (!cleaned.equals(e.getValue())) {
                System.out.println("[XSS] sanitized param '" + e.getKey() + "'");
            }
            request.setParam(e.getKey(), cleaned);
        }
        return true;
    }
}
