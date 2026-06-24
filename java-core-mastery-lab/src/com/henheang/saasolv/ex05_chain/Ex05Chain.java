package com.henheang.saasolv.ex05_chain;

/**
 * EXERCISE 05 — Filter/Interceptor chain (Chain of Responsibility).
 * Concept source: saas-olv XssFilter + AuthInterceptor + LoggingInterceptor.
 */
public class Ex05Chain {

    public static void main(String[] args) {
        // order matters: sanitize -> authenticate -> log
        InterceptorChain chain = new InterceptorChain()
                .add(new XssInterceptor())
                .add(new AuthInterceptor())
                .add(new LoggingInterceptor());

        System.out.println("===== Request 1: logged-in user =====");
        Request ok = new Request("/board/list.do", true);
        ok.setParam("keyword", "<script>alert(1)</script>");
        chain.dispatch(ok);

        System.out.println("\n===== Request 2: anonymous user =====");
        Request blocked = new Request("/admin/users.do", false);
        blocked.setParam("keyword", "hello");
        chain.dispatch(blocked); // AUTH stops it; LOG never runs

        /*
         * 🔧 PRACTICE IDEAS
         *  - Add a RateLimitInterceptor that blocks after N calls.
         *  - Add postHandle()/afterCompletion() to the interface and run them in
         *    REVERSE order (like real Spring interceptors).
         */
    }
}
