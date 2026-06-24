package com.henheang.saasolv.ex06_threadlocal;

/**
 * Mirrors saas-olv: egovframework.com.cmm.context.SessionHolder
 *
 * Stores per-request user info in a ThreadLocal so ANY layer (controller,
 * service, mapper) can read it without passing it through every method.
 *
 * Demonstrates THREE idioms at once:
 *   1) ThreadLocal           — one value per thread, isolated between threads
 *   2) utility-class idiom    — private constructor, all-static API
 *   3) static nested class    — Context holds the actual data
 *
 * ⚠️ You MUST call clear() at request end or thread-pool reuse leaks data.
 */
public final class SessionHolder {

    private SessionHolder() { } // no instances

    private static final ThreadLocal<Context> CONTEXT = new ThreadLocal<>();

    public static class Context {
        private String userId;
        private String userIp;
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getUserIp() { return userIp; }
        public void setUserIp(String userIp) { this.userIp = userIp; }
    }

    public static void init(Context ctx) { CONTEXT.set(ctx); }

    public static void clear() { CONTEXT.remove(); }

    public static String getUserId() {
        Context ctx = CONTEXT.get();
        return ctx != null ? ctx.getUserId() : null;
    }

    public static String getUserIp() {
        Context ctx = CONTEXT.get();
        return ctx != null ? ctx.getUserIp() : null;
    }
}
