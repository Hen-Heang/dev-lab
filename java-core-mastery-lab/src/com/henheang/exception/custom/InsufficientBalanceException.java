package com.henheang.exception.custom;

import java.math.BigDecimal;

/**
 * Custom UNCHECKED exception (extends RuntimeException).
 *
 * Use unchecked for programming/business errors the caller usually can't recover
 * from inline. Carries DOMAIN DATA (the shortfall) so the handler can react —
 * same idea as saas-olv's CmmBizException carrying an error code.
 */
public class InsufficientBalanceException extends RuntimeException {

    private final BigDecimal shortfall;

    public InsufficientBalanceException(BigDecimal balance, BigDecimal requested) {
        super("Insufficient balance: have " + balance + ", need " + requested);
        this.shortfall = requested.subtract(balance);
    }

    public BigDecimal getShortfall() {
        return shortfall;
    }
}
