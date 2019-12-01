package com.revolut.money.transfer.api.response;

import java.io.Serializable;
import java.util.Objects;

public class MoneyResponse implements Serializable {

    private static final long serialVersionUID = -6907466017622695132L;
    private String currency;
    private String amount;

    public MoneyResponse() {
    }

    public MoneyResponse(String currency, String amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MoneyResponse that = (MoneyResponse) o;
        return Objects.equals(currency, that.currency) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, amount);
    }

    @Override public String toString() {
        return "MoneyResponse{" +
                "currency='" + currency + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
