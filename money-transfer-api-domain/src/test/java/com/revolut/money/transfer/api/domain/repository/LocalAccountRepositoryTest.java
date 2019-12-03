package com.revolut.money.transfer.api.domain.repository;

import com.revolut.money.transfer.api.domain.fixture.AccountFixture;
import com.revolut.money.transfer.api.domain.model.Account;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;

@RunWith(MockitoJUnitRunner.class)
public class LocalAccountRepositoryTest {

    private AccountRepository localAccountRepository;

    @Before
    public void setupBeforeTest() {
        localAccountRepository = new LocalAccountRepository();
    }

    @Test
    public void shouldGetAllAccounts_whenExists() {
        localAccountRepository.create(AccountFixture.buildAccount());
        localAccountRepository.create(AccountFixture.buildAccount());
        localAccountRepository.create(AccountFixture.buildAccount());
        localAccountRepository.create(AccountFixture.buildAccount());
        localAccountRepository.create(AccountFixture.buildAccount());
        localAccountRepository.create(AccountFixture.buildAccount());

        List<Account> accounts = localAccountRepository.getAll();

        assertNotNull(accounts);
        assertTrue(accounts.size() > 0);
        assertEquals(6, accounts.size());
    }

    @Test
    public void shouldSaveAccount_whenCreateValidAccount() {
        String number = UUID.randomUUID().toString();
        localAccountRepository.create(AccountFixture.buildAccountWithNumber(number));
        Account accountCreated = localAccountRepository.findByNumber(number)
                .orElse(null);

        assertNotNull(accountCreated);
        assertEquals(number, accountCreated.getNumber());
    }

    @Test
    public void shouldNotGetAnyAccount_whenAccountNotExist() {
        localAccountRepository.create(AccountFixture.buildAccount());
        Account accountRetrieved = localAccountRepository.findByNumber("anotherNumber")
                .orElse(null);

        assertNull(accountRetrieved);
    }

    @Test
    public void shouldUpdateAccount_whenAccountHasChanged() {
        String number = UUID.randomUUID().toString();
        Account originalAccount = AccountFixture.buildAccountWithNumber(number);
        localAccountRepository.create(AccountFixture.buildAccountWithNumber(number));

        Account accountWithChanges = Account.builder()
                .withNumber(number)
                .withOwner("owner")
                .withFunds(Money.of(500, "USD"))
                .createdAt(LocalDateTime.now())
                .build();

        localAccountRepository.update(accountWithChanges);
        Account accountUpdated = localAccountRepository.findByNumber(number)
                .orElse(null);

        assertNotNull(accountUpdated);
        assertNotEquals(originalAccount, accountUpdated);
        assertThat(accountUpdated, hasProperty("funds", equalTo(Money.of(500, "USD"))));
    }
}
