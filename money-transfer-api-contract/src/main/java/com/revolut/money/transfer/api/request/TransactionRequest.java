package com.revolut.money.transfer.api.request;

import java.io.Serializable;
import java.util.Objects;

public class TransactionRequest implements Serializable {

    private static final long serialVersionUID = 1158497505315075612L;
    private String origin;
    private String destination;
    private MoneyRequest amount;

    public TransactionRequest() {
    }

    public TransactionRequest(String origin, String destination, MoneyRequest amount) {
        this.origin = origin;
        this.destination = destination;
        this.amount = amount;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public MoneyRequest getAmount() {
        return amount;
    }

    public void setAmount(MoneyRequest amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TransactionRequest that = (TransactionRequest) o;
        return Objects.equals(origin, that.origin) &&
                Objects.equals(destination, that.destination) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destination, amount);
    }

    @Override
    public String toString() {
        return "TransactionRequest{" +
                "origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", amount=" + amount +
                '}';
    }
}
