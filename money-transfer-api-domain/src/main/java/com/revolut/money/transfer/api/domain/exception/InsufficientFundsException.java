package com.revolut.money.transfer.api.domain.exception;

import com.revolut.money.transfer.api.domain.util.Errors;
import static java.lang.String.format;

public class InsufficientFundsException extends RevolutException {

    private static final long serialVersionUID = 1290544702621000772L;
    private static final Errors ERROR = Errors.INSUFFICIENT_FUNDS;

    public InsufficientFundsException(String number) {
        super(ERROR.getCode(), format(ERROR.getDescription(), number));
    }
}
