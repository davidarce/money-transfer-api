package com.revolut.money.transfer.api.domain.service;

import com.revolut.money.transfer.api.domain.exception.AccountNotFoundException;
import com.revolut.money.transfer.api.domain.model.Account;
import com.revolut.money.transfer.api.domain.repository.AccountRepository;

import java.util.List;

public class AccountQueryService {

    private final AccountRepository accountRepository;

    public AccountQueryService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.getAll();
    }

    public Account findAccountByNumber(String number) {
        return accountRepository.findByNumber(number)
                .orElseThrow(() -> new AccountNotFoundException(number));
    }

}
