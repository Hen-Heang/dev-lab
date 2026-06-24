package com.henheang.saasolv.ex01_generics;

import java.util.HashMap;
import java.util.Map;

/**
 * Mirrors saas-olv: egovframework.com.cmm.service.CmmInVO&lt;T extends CmmVO&gt;
 * <p>
 * A generic request wrapper. The {@code body} can be ANY VO type, but the
 * BOUNDED TYPE PARAMETER {@code <T extends BaseVO>} guarantees at COMPILE TIME
 * that whatever you put in is a real VO — you can never accidentally pass a
 * String or an unrelated type.
 *
 * @param <T> a VO type that extends {@link BaseVO}
 */
public class ApiRequest<T extends BaseVO> {

    private T body;
    private Map<String, Object> page;

    public T getBody() { return body; }
    public void setBody(T body) { this.body = body; }

    /** Defensive: never return null (same trick CmmInVO uses). */
    public Map<String, Object> getPage() {
        return page != null ? page : new HashMap<>();
    }
    public void setPage(Map<String, Object> page) { this.page = page; }
}
