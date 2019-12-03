package com.revolut.money.transfer.api.domain.service;

import com.revolut.money.transfer.api.domain.exception.AccountNotFoundException;
import com.revolut.money.transfer.api.domain.exception.CurrencyAccountException;
import com.revolut.money.transfer.api.domain.exception.CurrencyTransactionException;
import com.revolut.money.transfer.api.domain.exception.InsufficientFundsException;
import com.revolut.money.transfer.api.domain.model.Account;
import com.revolut.money.transfer.api.domain.model.Transaction;
import com.revolut.money.transfer.api.domain.repository.AccountRepository;
import com.revolut.money.transfer.api.domain.repository.TransactionRepository;
import com.revolut.money.transfer.api.domain.util.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.concurrent.TimeUnit;

public class TransactionCommandService {

    private final Logger LOG = LoggerFactory.getLogger(TransactionCommandService.class);

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionCommandService(TransactionRepository transactionRepository,
            AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public void createTransaction(Transaction transaction) {
        LOG.info("Transaction with id {} - STARTED {}", transaction.getId(), Thread.currentThread().getId());

        while (TransactionStatus.PROCESSING.equals(transaction.getStatus())) {
            validateTransaction(transaction);

            Account originAccount = getAccount(transaction.getOrigin());
            Account destinationAccount = getAccount(transaction.getDestination());

            if (originAccount.getLock().tryLock()) {
                try {
                    if (destinationAccount.getLock().tryLock()) {
                        try {
                            createTransfer(originAccount, destinationAccount, transaction);
                        } finally {
                            destinationAccount.getLock().unlock();
                        }
                    }
                } finally {
                    originAccount.getLock().unlock();
                }
            }
        }
        LOG.info("Transaction with id {} accounts [{} - {}] CREATED {}", transaction.getId(), transaction.getOrigin(),
                transaction.getDestination(), Thread.currentThread().getId());
    }

    private synchronized void validateTransaction(Transaction transaction) {
        beginTransaction(transaction);
        getAccount(transaction.getOrigin());
        getAccount(transaction.getDestination());
    }

    private void beginTransaction(Transaction transaction) {
        Account origin = getAccount(transaction.getOrigin());
        Account destination = getAccount(transaction.getDestination());
        validateCurrencyAccounts(origin.getFunds().getCurrency(), destination.getFunds().getCurrency());
        validateFunds(transaction, origin);
    }

    private Account getAccount(final String number) {
        return accountRepository.findByNumber(number)
                .orElseThrow(() -> new AccountNotFoundException(number));
    }

    private synchronized void createTransfer(Account origin, Account destination, Transaction transaction) {
        try {
            TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 1000));
            subtractFunds(origin, transaction.getAmount());
            depositFunds(destination, transaction.getAmount());
            transaction.setStatus(TransactionStatus.CREATED);
            transactionRepository.create(transaction);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private void subtractFunds(Account origin, MonetaryAmount amount) {
        accountRepository.update(Account.builder()
                .withNumber(origin.getNumber())
                .withOwner(origin.getOwner())
                .withFunds(origin.getFunds().subtract(amount))
                .createdAt(origin.getCreatedAt())
                .build());
    }

    private void depositFunds(Account destination, MonetaryAmount amount) {
        accountRepository.update(Account.builder()
                .withNumber(destination.getNumber())
                .withOwner(destination.getOwner())
                .withFunds(destination.getFunds().add(amount))
                .createdAt(destination.getCreatedAt())
                .build());
    }

    private void validateFunds(Transaction transaction, Account origin) {
        if (!origin.getFunds().getCurrency().getCurrencyCode().equals(
                transaction.getAmount().getCurrency().getCurrencyCode())) {
            throw new CurrencyTransactionException();
        } else {

            if (origin.getFunds().isLessThan(transaction.getAmount())) {
                throw new InsufficientFundsException(origin.getNumber());
            }
        }
    }

    private void validateCurrencyAccounts(CurrencyUnit origin, CurrencyUnit destination) {
        if (!origin.getCurrencyCode().equals(destination.getCurrencyCode())) {
            throw new CurrencyAccountException();
        }
    }
}
