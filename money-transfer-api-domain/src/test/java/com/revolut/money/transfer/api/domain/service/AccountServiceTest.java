package com.revolut.money.transfer.api.domain.service;

import com.revolut.money.transfer.api.domain.exception.AccountAlreadyCreatedException;
import com.revolut.money.transfer.api.domain.exception.AccountNotFoundException;
import com.revolut.money.transfer.api.domain.fixture.AccountFixture;
import com.revolut.money.transfer.api.domain.model.Account;
import com.revolut.money.transfer.api.domain.repository.AccountRepository;
import com.revolut.money.transfer.api.domain.repository.LocalAccountRepository;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    private final AccountRepository localAccountRepository = mock(LocalAccountRepository.class);

    private final AccountQueryService accountQueryService = new AccountQueryService(localAccountRepository);
    private final AccountCommandService accountCommandService = new AccountCommandService(localAccountRepository);

    @Test
    public void shouldGetAllAccountsCreatedInLocalRepository() {
        when(localAccountRepository.getAll()).thenReturn(AccountFixture.buildAccountList());

        List<Account> accounts = accountQueryService.getAllAccounts();

        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
        assertEquals(6, accounts.size());
    }

    @Test
    public void shouldGetAccount_whenAccountWithNumberExistsInRepository() {
        String accountNumber = "newId";
        when(localAccountRepository.findByNumber(accountNumber)).thenReturn(
                Optional.of(AccountFixture.buildAccountWithNumber(accountNumber)));

        Account accountRetrieved = accountQueryService.findAccountByNumber(accountNumber);

        assertNotNull(accountRetrieved);
        assertEquals(accountNumber, accountRetrieved.getNumber());
    }

    @Test(expected = AccountNotFoundException.class)
    public void shouldThrowNotFoundException_whenAccountWithNumberNotExistsInRepository() {
        when(localAccountRepository.findByNumber("noExists")).thenReturn(Optional.empty());

        accountQueryService.findAccountByNumber("noExists");
    }

    @Test
    public void shouldCreateAnAccountValid() {
        Account account = AccountFixture.buildAccount();

        when(localAccountRepository.findByNumber(account.getNumber())).thenReturn(Optional.empty());

        accountCommandService.createAccount(account);

        verify(localAccountRepository, atMost(1)).create(account);
        verify(localAccountRepository).findByNumber(account.getNumber());
    }

    @Test(expected = AccountAlreadyCreatedException.class)
    public void shouldNotCreateAnAccount_whenAccountWithNumberAlreadyExists() {
        Account account = AccountFixture.buildAccountWithNumber("aNumber");

        when(localAccountRepository.findByNumber(account.getNumber())).thenReturn(
                Optional.of(AccountFixture.buildAccountWithNumber("aNumber")));

        accountCommandService.createAccount(account);

        verify(localAccountRepository, never()).create(account);
        verify(localAccountRepository).findByNumber(account.getNumber());
    }
}
