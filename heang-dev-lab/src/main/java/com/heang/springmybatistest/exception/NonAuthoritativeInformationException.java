package com.heang.springmybatistest.exception;

public class NonAuthoritativeInformationException extends RuntimeException{
    public NonAuthoritativeInformationException(String message){
        super(message);
    }
}
