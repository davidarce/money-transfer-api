package com.revolut.money.transfer.api.domain.repository;

import com.revolut.money.transfer.api.domain.model.Account;

import java.util.*;

public class LocalAccountRepository implements AccountRepository {

    private Map<String, Account> accounts = new HashMap<>();

    @Override
    public List<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public void create(Account account) {
        accounts.put(account.getNumber(), account);
    }

    @Override
    public Optional<Account> findByNumber(String number) {
        return Optional.ofNullable(accounts.get(number));
    }

    @Override
    public void update(Account account) {
        accounts.put(account.getNumber(), account);
    }
}
