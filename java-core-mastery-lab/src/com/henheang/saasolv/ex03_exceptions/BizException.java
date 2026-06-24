package com.henheang.saasolv.ex03_exceptions;

/**
 * Mirrors saas-olv: egovframework.com.cmm.exception.CmmBizException
 *
 * A BUSINESS exception (unchecked). Carries a domain error code (M####).
 * Thrown when a business rule is violated -> maps to HTTP 400.
 */
public class BizException extends RuntimeException {

    private final String code;

    public BizException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() { return code; }
}
