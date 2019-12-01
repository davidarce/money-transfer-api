package com.revolut.money.transfer.api.request;

import java.io.Serializable;
import java.util.Objects;

public class AccountRequest implements Serializable {

    private static final long serialVersionUID = 4288839708185996379L;

    private String owner;
    private MoneyRequest funds;

    public AccountRequest() {
    }

    public AccountRequest(String owner, MoneyRequest funds) {
        this.owner = owner;
        this.funds = funds;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public MoneyRequest getFunds() {
        return funds;
    }

    public void setFunds(MoneyRequest funds) {
        this.funds = funds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AccountRequest that = (AccountRequest) o;
        return Objects.equals(owner, that.owner) &&
                Objects.equals(funds, that.funds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, funds);
    }

    @Override public String toString() {
        return "AccountRequest{" +
                "owner='" + owner + '\'' +
                ", funds=" + funds +
                '}';
    }
}
