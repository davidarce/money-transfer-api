package com.revolut.money.transfer.api.response;

import java.io.Serializable;
import java.util.Objects;

public class AccountResponse implements Serializable {

    private static final long serialVersionUID = -8632954831208871807L;

    private String number;
    private String owner;
    private String createdAt;
    private String funds;

    public AccountResponse() {
    }

    public AccountResponse(String number, String owner, String createdAt,
            String funds) {
        this.number = number;
        this.owner = owner;
        this.createdAt = createdAt;
        this.funds = funds;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFunds() {
        return funds;
    }

    public void setFunds(String funds) {
        this.funds = funds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AccountResponse that = (AccountResponse) o;
        return Objects.equals(number, that.number) &&
                Objects.equals(owner, that.owner) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(funds, that.funds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, owner, createdAt, funds);
    }

    @Override
    public String toString() {
        return "AccountResponse{" +
                "number='" + number + '\'' +
                ", owner='" + owner + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", funds=" + funds +
                '}';
    }
}
