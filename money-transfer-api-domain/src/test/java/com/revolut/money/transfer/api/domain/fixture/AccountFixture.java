package com.revolut.money.transfer.api.domain.fixture;

import com.revolut.money.transfer.api.domain.model.Account;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AccountFixture {

    public static List<Account> buildAccountList() {
        return Arrays.asList(
                buildAccount(),
                buildAccount(),
                buildAccount(),
                buildAccount(),
                buildAccount(),
                buildAccount());
    }

    public static Account buildAccount() {
        return Account.builder()
                .withNumber(UUID.randomUUID().toString())
                .withOwner("aOwner")
                .createdAt(LocalDateTime.now())
                .withFunds(Money.of(1000, "USD"))
                .build();
    }

    public static Account buildAccountWithNumber(String number) {
        return Account.builder()
                .withNumber(number)
                .withOwner("aOwner")
                .createdAt(LocalDateTime.now())
                .withFunds(Money.of(1000, "USD"))
                .build();
    }

    public static Account buildAccountWithNumberAndCurrencyFunds(String number, String currency) {
        return Account.builder()
                .withNumber(number)
                .withOwner("aOwner")
                .createdAt(LocalDateTime.now())
                .withFunds(Money.of(1000, currency))
                .build();
    }

    public static Account buildAccountWithNumberAndFunds(String number, MonetaryAmount funds) {
        return Account.builder()
                .withNumber(number)
                .withOwner("aOwner")
                .createdAt(LocalDateTime.now())
                .withFunds(funds)
                .build();
    }

}
