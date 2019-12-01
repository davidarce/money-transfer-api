package com.revolut.money.transfer.api.response;

import java.io.Serializable;
import java.util.Objects;

public class TransactionResponse implements Serializable {

    private static final long serialVersionUID = 4089811914318739371L;

    private String id;
    private String origin;
    private String destination;
    private String amount;
    private String status;
    private String createdAt;

    public TransactionResponse() {
    }

    public TransactionResponse(String id, String origin, String destination,
            String amount, String status, String createdAt) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TransactionResponse that = (TransactionResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(origin, that.origin) &&
                Objects.equals(destination, that.destination) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, origin, destination, amount, status, createdAt);
    }

    @Override public String toString() {
        return "TransactionResponse{" +
                "id='" + id + '\'' +
                "origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", amount='" + amount + '\'' +
                ", status='" + status + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
