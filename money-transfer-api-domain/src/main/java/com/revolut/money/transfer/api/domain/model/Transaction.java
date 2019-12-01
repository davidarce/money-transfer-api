package com.revolut.money.transfer.api.domain.model;

import com.google.common.base.Preconditions;
import com.revolut.money.transfer.api.domain.util.TransactionStatus;

import javax.money.MonetaryAmount;
import java.time.LocalDateTime;

public class Transaction {

    private final String id;
    private TransactionStatus status;
    private final String origin;
    private final String destination;
    private final MonetaryAmount amount;
    private final LocalDateTime createdAt;

    private Transaction(final Builder builder) {
        Preconditions.checkArgument(!builder.amount.isNegativeOrZero(), "Amount could not be negative or Zero");
        this.id = builder.id;
        this.status = builder.status;
        this.origin = builder.origin;
        this.destination = builder.destination;
        this.amount = builder.amount;
        this.createdAt = builder.createdAt;
    }

    public Transaction() {
        this(builder());
    }

    public String getId() {
        return id;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public MonetaryAmount getAmount() {
        return amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public static class Builder {
        private String id;
        private TransactionStatus status;
        private String origin;
        private String destination;
        private MonetaryAmount amount;
        private LocalDateTime createdAt;

        private Builder() {
            this.createdAt = LocalDateTime.now();
            this.status = TransactionStatus.PROCESSING;
        }

        public Builder withId(final String id) {
            this.id = id;
            return this;
        }

        public Builder withOrigin(final String origin) {
            this.origin = origin;
            return this;
        }

        public Builder withDestination(final String destination) {
            this.destination = destination;
            return this;
        }

        public Builder withAmount(final MonetaryAmount amount) {
            this.amount = amount;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }
}
