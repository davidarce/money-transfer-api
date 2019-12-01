package com.revolut.money.transfer.api.domain.repository;

import com.revolut.money.transfer.api.domain.model.Transaction;
import java.util.*;

public class LocalTransactionRepository implements TransactionRepository {

    private Map<String, Transaction> transactions = new HashMap<>();

    @Override
    public List<Transaction> getAll() {
        return new ArrayList<>(transactions.values());
    }

    @Override
    public void create(Transaction transaction) {
        transactions.put(transaction.getId(), transaction);
    }

    @Override
    public Optional<Transaction> findById(String id) {
        return Optional.ofNullable(transactions.get(id));
    }
}
