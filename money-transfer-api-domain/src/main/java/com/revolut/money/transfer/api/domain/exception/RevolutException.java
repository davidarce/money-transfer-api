package com.revolut.money.transfer.api.domain.exception;

public class RevolutException extends RuntimeException {

    private static final long serialVersionUID = 2747722999539241898L;
    private int code;

    public RevolutException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
