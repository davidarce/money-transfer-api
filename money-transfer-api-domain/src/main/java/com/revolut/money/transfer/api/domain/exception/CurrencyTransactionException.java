package com.revolut.money.transfer.api.domain.exception;

import com.revolut.money.transfer.api.domain.util.Errors;

import static java.lang.String.format;

public class CurrencyTransactionException extends RevolutException {

    private static final long serialVersionUID = 72340816140820305L;
    private static final Errors ERROR = Errors.DIFFERENT_CURRENCY_TRANSACTION;

    public CurrencyTransactionException() {
        super(ERROR.getCode(), ERROR.getDescription());
    }
}
