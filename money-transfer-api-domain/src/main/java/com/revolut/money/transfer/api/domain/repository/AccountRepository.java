package com.revolut.money.transfer.api.domain.repository;

import com.revolut.money.transfer.api.domain.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    List<Account> getAll();

    void create(Account account);

    Optional<Account> findByNumber(String number);

    void update(Account account);
}
