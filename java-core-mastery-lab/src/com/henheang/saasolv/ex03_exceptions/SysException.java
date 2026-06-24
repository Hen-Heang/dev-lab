package com.henheang.saasolv.ex03_exceptions;

/**
 * Mirrors saas-olv: egovframework.com.cmm.exception.CmmException
 *
 * A SYSTEM/infra exception (unchecked). Carries an error code (E####).
 * Thrown on technical failures -> maps to HTTP 500. Usually wraps a cause.
 */
public class SysException extends RuntimeException {

    private final String code;

    public SysException(String code, String message) {
        super(message);
        this.code = code;
    }

    public SysException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() { return code; }
}
