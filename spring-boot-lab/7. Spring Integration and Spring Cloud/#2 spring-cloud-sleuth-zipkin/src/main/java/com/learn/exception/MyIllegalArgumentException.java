package com.learn.exception;

public class MyIllegalArgumentException extends IllegalArgumentException{

    public MyIllegalArgumentException() {
        super("Parameter value %d did not match expected type");
    }
}
