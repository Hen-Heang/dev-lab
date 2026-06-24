package com.henheang.saasolv.ex03_exceptions;

/**
 * Mirrors saas-olv: egovframework.com.cmm.exception.CmmExceptionHandler (@ControllerAdvice)
 *
 * In Spring this is a @ControllerAdvice with @ExceptionHandler methods. Here we
 * simulate the SAME dispatch logic in plain Java so you can study the shape:
 *   - dispatch by exception TYPE
 *   - render JSON for AJAX requests, an HTML alert script for page requests
 *   - a catch-all for anything unexpected
 */
public class GlobalExceptionHandler {

    /**
     * @param ex     the thrown exception
     * @param isAjax true = JSON response, false = HTML alert+back (page request)
     * @return the rendered response body
     */
    public String handle(Throwable ex, boolean isAjax) {
        if (ex instanceof BizException biz) {                 // pattern matching (Java 16+)
            return render(400, biz.getCode(), biz.getMessage(), isAjax);
        } else if (ex instanceof SysException sys) {
            return render(500, sys.getCode(), sys.getMessage(), isAjax);
        } else {
            return render(500, "E9999", "시스템 오류가 발생하였습니다.", isAjax);
        }
    }

    private String render(int status, String code, String message, boolean isAjax) {
        if (isAjax) {
            return "{\"success\":false,\"status\":" + status
                    + ",\"errorCode\":\"" + code + "\",\"message\":\"" + message + "\"}";
        }
        // page request: bounce back with an alert (same trick as CmmExceptionHandler)
        return "<script>alert('" + message.replace("'", "\\'") + "'); history.back();</script>";
    }
}
