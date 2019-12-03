package com.revolut.money.transfer.api.domain.service;

import com.revolut.money.transfer.api.domain.exception.*;
import com.revolut.money.transfer.api.domain.fixture.AccountFixture;
import com.revolut.money.transfer.api.domain.fixture.TransactionFixture;
import com.revolut.money.transfer.api.domain.model.Account;
import com.revolut.money.transfer.api.domain.model.Transaction;
import com.revolut.money.transfer.api.domain.repository.AccountRepository;
import com.revolut.money.transfer.api.domain.repository.LocalAccountRepository;
import com.revolut.money.transfer.api.domain.repository.LocalTransactionRepository;
import com.revolut.money.transfer.api.domain.repository.TransactionRepository;
import com.revolut.money.transfer.api.domain.util.TransactionStatus;
import org.javamoney.moneta.Money;
import org.junit.Test;

import javax.money.MonetaryAmount;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    private final TransactionRepository localTransactionRepository = mock(LocalTransactionRepository.class);
    private final AccountRepository localAccountRepository = mock(LocalAccountRepository.class);

    private final TransactionQueryService transactionQueryService = new TransactionQueryService(
            localTransactionRepository);
    private final TransactionCommandService transactionCommandService = new TransactionCommandService(
            localTransactionRepository, localAccountRepository);

    @Test
    public void shouldGetAllTransactionCreatedInLocalRepository() {
        when(localTransactionRepository.getAll()).thenReturn(
                TransactionFixture.buildTransactionsList());

        List<Transaction> transactions = transactionQueryService.getAllTransactions();

        assertNotNull(transactions);
        assertFalse(transactions.isEmpty());
        assertEquals(4, transactions.size());
    }

    @Test
    public void shouldGetTransaction_whenTransactionWithIdExistsInRepository() {
        String idTransaction = "newId";
        when(localTransactionRepository.findById(idTransaction)).thenReturn(
                Optional.of(TransactionFixture.buildTransactionWithId(idTransaction)));

        Transaction transactionRetrieved = transactionQueryService.findTransactionById(idTransaction);

        assertNotNull(transactionRetrieved);
        assertEquals(idTransaction, transactionRetrieved.getId());
    }

    @Test(expected = TransactionNotFoundException.class)
    public void shouldThrowNotFoundException_whenTransactionWithIdNotExistsInRepository() {
        when(localTransactionRepository.findById("noExists")).thenReturn(Optional.empty());

       transactionQueryService.findTransactionById("noExists");
    }

    @Test
    public void shouldCreateATransactionValidBetweenAccounts() {
        Transaction transaction = TransactionFixture.buildTransaction();

        when(localAccountRepository.findByNumber(transaction.getOrigin())).thenReturn(
                Optional.of(AccountFixture.buildAccountWithNumber("anAccount")));
        when(localAccountRepository.findByNumber(transaction.getDestination())).thenReturn(
                Optional.of(AccountFixture.buildAccountWithNumber("anotherAccount")));

        transactionCommandService.createTransaction(transaction);

        assertEquals(TransactionStatus.CREATED, transaction.getStatus());
        verify(localAccountRepository, atMost(2)).update(any(Account.class));
        verify(localTransactionRepository).create(transaction);
    }

    @Test(expected = AccountNotFoundException.class)
    public void shouldNotCreateATransactionBetweenAccounts_whenAccountOriginNotFound() {
        Transaction transaction = TransactionFixture.buildTransaction();

        when(localAccountRepository.findByNumber(transaction.getOrigin())).thenReturn(
                Optional.empty());
        when(localAccountRepository.findByNumber(transaction.getDestination())).thenReturn(
                Optional.of(AccountFixture.buildAccountWithNumber("anotherAccount")));

        transactionCommandService.createTransaction(transaction);
    }

    @Test(expected = CurrencyAccountException.class)
    public void shouldNotCreateATransactionBetweenAccounts_whenAccountOriginAndAccountDestinationHaveDifferentCurrencies() {
        Transaction transaction = TransactionFixture.buildTransaction();

        when(localAccountRepository.findByNumber(transaction.getOrigin())).thenReturn(
                Optional.of(AccountFixture.buildAccountWithNumberAndCurrencyFunds("anAccount", "COP")));
        when(localAccountRepository.findByNumber(transaction.getDestination())).thenReturn(
                Optional.of(AccountFixture.buildAccountWithNumber("anotherAccount")));

        transactionCommandService.createTransaction(transaction);
    }

    @Test(expected = CurrencyTransactionException.class)
    public void shouldNotCreateATransactionBetweenAccounts_whenAccountOriginAndCurrencyTransactionAreDifferent() {
        Transaction transaction = TransactionFixture.buildTransaction();

        when(localAccountRepository.findByNumber(transaction.getOrigin())).thenReturn(
                Optional.of(AccountFixture.buildAccountWithNumberAndCurrencyFunds("anAccount", "COP")));
        when(localAccountRepository.findByNumber(transaction.getDestination())).thenReturn(
                Optional.of(AccountFixture.buildAccountWithNumberAndCurrencyFunds("anotherAccount", "COP")));

        transactionCommandService.createTransaction(transaction);
    }

    @Test(expected = InsufficientFundsException.class)
    public void shouldNotCreateATransactionBetweenAccounts_whenAccountOriginDoesNotHaveFunds() {
        Transaction transaction = TransactionFixture.buildTransaction();
        MonetaryAmount funds = Money.of(10, "USD");

        when(localAccountRepository.findByNumber(transaction.getOrigin())).thenReturn(
                Optional.of(AccountFixture.buildAccountWithNumberAndFunds("anAccount", funds)));
        when(localAccountRepository.findByNumber(transaction.getDestination())).thenReturn(
                Optional.of(AccountFixture.buildAccountWithNumber("anotherAccount")));

        transactionCommandService.createTransaction(transaction);
    }
}
