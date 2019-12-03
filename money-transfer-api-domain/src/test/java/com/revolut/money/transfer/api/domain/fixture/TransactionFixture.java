package com.revolut.money.transfer.api.domain.fixture;

import com.revolut.money.transfer.api.domain.model.Transaction;
import org.javamoney.moneta.Money;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TransactionFixture {

    public static List<Transaction> buildTransactionsList() {
        return Arrays.asList(
                buildTransaction(),
                buildTransaction(),
                buildTransaction(),
                buildTransaction());
    }

    public static Transaction buildTransaction() {
        return Transaction.builder()
                .withId(UUID.randomUUID().toString())
                .withOrigin("accountOrigin")
                .withDestination("accountDestination")
                .withAmount(Money.of(100, "USD"))
                .build();
    }

    public static Transaction buildTransactionWithId(String id) {
        return Transaction.builder()
                .withId(id)
                .withOrigin("accountOrigin")
                .withDestination("accountDestination")
                .withAmount(Money.of(100, "USD"))
                .build();
    }
}
