package com.revolut.money.transfer.api.domain.service;

import com.revolut.money.transfer.api.domain.exception.TransactionNotFoundException;
import com.revolut.money.transfer.api.domain.model.Transaction;
import com.revolut.money.transfer.api.domain.repository.TransactionRepository;

import java.util.List;

public class TransactionQueryService {

    private final TransactionRepository transactionRepository;

    public TransactionQueryService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.getAll();
    }

    public Transaction findTransactionById(String id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

}
