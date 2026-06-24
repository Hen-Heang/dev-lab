package com.henheang.saasolv.ex05_chain;

import java.util.ArrayList;
import java.util.List;

/**
 * Runs interceptors in registration order. If any returns false the chain
 * stops and the request is rejected — exactly how Spring MVC's interceptor
 * pipeline + servlet Filter chain behave (@Order controls the sequence).
 */
public class InterceptorChain {

    private final List<Interceptor> interceptors = new ArrayList<>();

    public InterceptorChain add(Interceptor interceptor) {
        interceptors.add(interceptor);
        return this; // fluent
    }

    /** @return true if the request passed every interceptor. */
    public boolean dispatch(Request request) {
        for (Interceptor interceptor : interceptors) {
            if (!interceptor.preHandle(request)) {
                return false; // short-circuit
            }
        }
        System.out.println("--> reached controller: " + request.getPath());
        return true;
    }
}
