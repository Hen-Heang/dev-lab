package com.henheang.saasolv.ex01_generics;

import java.util.HashMap;
import java.util.Map;

/**
 * Mirrors saas-olv: egovframework.com.cmm.service.CmmOutVO
 * <p>
 * Demonstrates the STATIC FACTORY METHOD idiom (Effective Java, Item 1):
 * instead of {@code new ApiResponse(...)} callers use the readable
 * {@code ApiResponse.of(list, page)}.
 *
 * @param <T> the payload type carried in {@code body}
 */
public class ApiResponse<T> {

    private T body;
    private Map<String, Object> page;

    private ApiResponse() { } // force creation through the factory

    public T getBody() { return body; }
    public Map<String, Object> getPage() { return page; }

    /** Static factory — named, self-documenting construction. */
    public static <T> ApiResponse<T> of(T body, Map<String, Object> page) {
        ApiResponse<T> r = new ApiResponse<>();
        r.body = body;
        r.page = page;
        return r;
    }
}
