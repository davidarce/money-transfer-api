package com.revolut.money.transfer.api.request;

import java.io.Serializable;
import java.util.Objects;

public class MoneyRequest implements Serializable {

    private String currency;
    private String value;

    public MoneyRequest() {
    }

    public MoneyRequest(String currency, String value) {
        this.currency = currency;
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public MoneyRequest setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public String getValue() {
        return value;
    }

    public MoneyRequest setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MoneyRequest that = (MoneyRequest) o;
        return Objects.equals(currency, that.currency) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, value);
    }

    @Override
    public String toString() {
        return "MoneyRequest{" +
                "currency='" + currency + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
