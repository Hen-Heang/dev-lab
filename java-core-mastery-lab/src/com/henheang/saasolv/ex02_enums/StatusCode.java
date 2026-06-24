package com.henheang.saasolv.ex02_enums;

/**
 * Mirrors saas-olv: egovframework.com.lnk.cmm.code.ApiResponseCode
 *
 * A "typesafe enum with behavior". Each constant carries a code + message,
 * there is a reverse-lookup factory {@link #of(String)} that never returns
 * null (falls back to UNKNOWN), and a behavior method {@link #isSuccess()}.
 *
 * This is far safer than scattering String constants like "0000" around.
 */
public enum StatusCode {

    SUCCESS("0000", "성공 / OK"),
    BAD_REQUEST("M9000", "입력값이 올바르지 않습니다."),
    UNAUTHORIZED("F2001", "토큰 인증 실패"),
    TOKEN_EXPIRED("F2002", "토큰 만료"),
    SYSTEM_ERROR("E9999", "시스템 오류가 발생하였습니다."),
    UNKNOWN("UNKNOWN", "알 수 없는 응답코드");

    private final String code;
    private final String message;

    StatusCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }

    /** Reverse lookup: code String -> enum. Safe default = UNKNOWN. */
    public static StatusCode of(String code) {
        if (code == null) return UNKNOWN;
        for (StatusCode s : values()) {
            if (s.code.equals(code)) return s;
        }
        return UNKNOWN;
    }

    /** Behavior lives on the enum, not in scattered if-statements. */
    public boolean isSuccess() {
        return this == SUCCESS;
    }
}
