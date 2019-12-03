package com.revolut.money.transfer.api.transformer;

import com.revolut.money.transfer.api.domain.model.Account;
import com.revolut.money.transfer.api.request.AccountRequest;
import com.revolut.money.transfer.api.request.MoneyRequest;
import com.revolut.money.transfer.api.response.AccountResponse;
import org.javamoney.moneta.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class AccountTransformerTest {

    private AccountTransformer accountTransformer = new AccountTransformer();

    @Test
    public void shouldTransformAccountRequestToAccountDomainValid() {
        AccountRequest request = new AccountRequest()
                .setOwner("aOwner")
                .setFunds(new MoneyRequest()
                        .setValue("1000")
                        .setCurrency("USD"));

        Account account = accountTransformer.toAccountDomain(request);

        assertNotNull(account);
        assertNotNull(account.getNumber());
        assertThat(account, hasProperty("funds", equalTo(Money.of(1000, "USD"))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreatedAccountDomain_whenAccountRequestIsNotValid() {
        accountTransformer.toAccountDomain(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreatedAccountDomain_whenAccountFundsIsNotValid() {
        AccountRequest request = new AccountRequest()
                .setOwner("aAccountOrigin")
                .setFunds(null);

        accountTransformer.toAccountDomain(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreatedAccountDomain_whenAccountOwnerIsNotValid() {
        AccountRequest request = new AccountRequest()
                .setOwner("")
                .setFunds(new MoneyRequest()
                        .setValue("1000")
                        .setCurrency("USD"));

        accountTransformer.toAccountDomain(request);
    }

    @Test
    public void shouldTransformAccountDomainToAccountResponseValid() {
        Account account = Account.builder()
                .withNumber("accountNumber")
                .withOwner("aOwner")
                .createdAt(LocalDateTime.now())
                .withFunds(Money.of(1000, "USD"))
                .build();

        AccountResponse response = accountTransformer.toAccountResponse(
                account, "USA", "en");

        assertNotNull(response);
        assertEquals("accountNumber", response.getNumber());
        assertEquals("aOwner", response.getOwner());
        assertThat(response, hasProperty("funds", equalTo("USD1,000.00")));
    }
}
