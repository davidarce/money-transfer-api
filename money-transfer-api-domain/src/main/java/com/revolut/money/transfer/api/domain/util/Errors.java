package com.revolut.money.transfer.api.domain.util;

public enum Errors {

    ACCOUNT_ALREADY_CREATED(400_100, "Account with number %s is already created."),
    ACCOUNT_NOT_FOUND(404_100, "Account with number %s not found!"),
    TRANSACTION_NOT_FOUND(404_101, "Transaction with id %s not found!"),
    DIFFERENT_CURRENCY_TRANSACTION(400_101, "Account origin and transaction have different currencies"),
    DIFFERENT_CURRENCY_ACCOUNT(400_102, "Accounts have different currencies"),
    INSUFFICIENT_FUNDS(400_103, "Account with number %s does not have sufficient funds");

    private Integer code;
    private String description;

    Errors(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
