package com.learn.calculator;

public interface Operation {

    long apply(long num1, long num2);

    boolean handles(char operation);
}
