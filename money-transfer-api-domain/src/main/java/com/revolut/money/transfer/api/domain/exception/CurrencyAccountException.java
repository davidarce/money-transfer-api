package com.revolut.money.transfer.api.domain.exception;

import com.revolut.money.transfer.api.domain.util.Errors;

public class CurrencyAccountException extends RevolutException {

    private static final long serialVersionUID = 4612569318230939123L;
    private static final Errors ERROR = Errors.DIFFERENT_CURRENCY_ACCOUNT;

    public CurrencyAccountException() {
        super(ERROR.getCode(), ERROR.getDescription());
    }
}
