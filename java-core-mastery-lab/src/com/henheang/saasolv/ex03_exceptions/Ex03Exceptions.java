package com.henheang.saasolv.ex03_exceptions;

/**
 * EXERCISE 03 — Custom exception hierarchy + centralized handler.
 * Concept source: saas-olv CmmBizException / CmmException / CmmExceptionHandler.
 */
public class Ex03Exceptions {

    // pretend service methods that fail in different ways
    static void businessRuleFails() {
        throw new BizException("M0001", "이미 사용 중인 아이디입니다.");
    }

    static void infraFails() {
        try {
            throw new RuntimeException("connection refused");
        } catch (RuntimeException cause) {
            throw new SysException("E0001", "DB 연결에 실패했습니다.", cause); // wrap the cause
        }
    }

    public static void main(String[] args) {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        System.out.println("== BizException as AJAX ==");
        try { businessRuleFails(); }
        catch (Throwable ex) { System.out.println(handler.handle(ex, true)); }

        System.out.println("\n== BizException as page request ==");
        try { businessRuleFails(); }
        catch (Throwable ex) { System.out.println(handler.handle(ex, false)); }

        System.out.println("\n== SysException as AJAX ==");
        try { infraFails(); }
        catch (Throwable ex) { System.out.println(handler.handle(ex, true)); }

        System.out.println("\n== Unexpected (catch-all) ==");
        try { throw new IllegalStateException("boom"); }
        catch (Throwable ex) { System.out.println(handler.handle(ex, true)); }

        /*
         * 🔧 PRACTICE IDEAS
         *  - Add a ValidationException carrying a list of field errors and render
         *    them as a JSON array (like MethodArgumentNotValidException handling).
         *  - Print the wrapped cause chain for SysException (ex.getCause()).
         */
    }
}
