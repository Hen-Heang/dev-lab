package com.learn.utils;

import com.learn.exception.CustomMessageException;
import org.springframework.http.HttpStatus;

public class CustomMessageExceptionUtils {

    private CustomMessageExceptionUtils() {}

    public static CustomMessageException unauthorized() {
        CustomMessageException messageException = new CustomMessageException();
        messageException.setMessage("Unauthorized");
        messageException.setCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        return messageException;
    }

}
