package com.revolut.money.transfer.api.domain.model;

import javax.money.MonetaryAmount;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.base.Preconditions;

public class Account {

    private final String number;
    private final String owner;
    private final MonetaryAmount funds;
    private final LocalDateTime createdAt;
    private final ReentrantLock lock;

    private Account(final Builder builder) {
        Preconditions.checkArgument(!builder.funds.isNegative(), "Money could not be negative");
        this.number = builder.number;
        this.owner = builder.owner;
        this.funds = builder.funds;
        this.createdAt = builder.createdAt;
        this.lock = new ReentrantLock(true);
    }

    public Account() {
        this(builder());
    }

    public String getNumber() {
        return number;
    }

    public String getOwner() {
        return owner;
    }

    public MonetaryAmount getFunds() {
        return funds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String number;
        private String owner;
        private MonetaryAmount funds;
        private LocalDateTime createdAt;

        private Builder() {
        }

        public Builder withNumber(final String number) {
            this.number = number;
            return this;
        }

        public Builder withOwner(final String owner) {
            this.owner = owner;
            return this;
        }

        public Builder withFunds(final MonetaryAmount funds) {
            this.funds = funds;
            return this;
        }

        public Builder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Account build() {
            return new Account(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Account account = (Account) o;
        return Objects.equals(number, account.number) &&
                Objects.equals(owner, account.owner) &&
                Objects.equals(funds, account.funds) &&
                Objects.equals(createdAt, account.createdAt);
    }

    @Override public int hashCode() {
        return Objects.hash(number, owner, funds, createdAt);
    }

    @Override public String toString() {
        return "Account{" +
                "number='" + number + '\'' +
                ", owner=" + owner +
                ", funds=" + funds +
                ", createdAt=" + createdAt +
                '}';
    }
}
