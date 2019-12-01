package com.revolut.money.transfer.api.domain.exception;

import com.revolut.money.transfer.api.domain.util.Errors;

import static java.lang.String.format;

public class AccountNotFoundException extends RevolutException {

    private static final long serialVersionUID = 5507166350756755870L;
    private static final Errors ERROR = Errors.ACCOUNT_NOT_FOUND;

    public AccountNotFoundException(String number) {
        super(ERROR.getCode(), format(ERROR.getDescription(), number));
    }
}
