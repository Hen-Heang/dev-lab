package com.learn;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A CUSTOM EXCEPTION for the "employee not found" situation.
 *
 * Why make your own exception? It gives errors a clear, named meaning instead
 * of a generic RuntimeException, and lets you control the HTTP response.
 *
 * @ResponseStatus(HttpStatus.NOT_FOUND): when this exception bubbles up out of
 * a controller and isn't caught, Spring automatically responds with HTTP 404
 * Not Found - no extra error-handling code needed.
 *
 * It extends `Exception` (not RuntimeException), making it a CHECKED exception:
 * the compiler forces callers to either catch it or declare `throws` - that is
 * why the service and controller methods above say `throws EmployeeRecordNotFoundException`.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EmployeeRecordNotFoundException extends Exception {

    // Constructor with just a message.
    public EmployeeRecordNotFoundException(String msg) {
        super(msg);
    }
    // Constructor that also wraps the original cause (useful for debugging chains).
    public EmployeeRecordNotFoundException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
