package com.revolut.money.transfer.api.domain.repository;

import com.revolut.money.transfer.api.domain.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    List<Transaction> getAll();

    void create(Transaction transaction);

    Optional<Transaction> findById(String id);

}
