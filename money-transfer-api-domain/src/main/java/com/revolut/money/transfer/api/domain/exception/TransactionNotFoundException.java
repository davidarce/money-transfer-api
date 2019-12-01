package com.revolut.money.transfer.api.domain.exception;

import com.revolut.money.transfer.api.domain.util.Errors;

import static java.lang.String.format;

public class TransactionNotFoundException extends RevolutException {

    private static final long serialVersionUID = 5507166350756755870L;
    private static final Errors ERROR = Errors.TRANSACTION_NOT_FOUND;

    public TransactionNotFoundException(String id) {
        super(ERROR.getCode(), format(ERROR.getDescription(), id));
    }
}
