package com.revolut.money.transfer.api.domain.exception;

import com.revolut.money.transfer.api.domain.util.Errors;

import static java.lang.String.format;

public class AccountAlreadyCreatedException extends RevolutException {

    private static final long serialVersionUID = -8359019618639794338L;

    private static final Errors ERROR = Errors.ACCOUNT_ALREADY_CREATED;

    public AccountAlreadyCreatedException(String number) {
        super(ERROR.getCode(), format(ERROR.getDescription(), number));
    }
}
