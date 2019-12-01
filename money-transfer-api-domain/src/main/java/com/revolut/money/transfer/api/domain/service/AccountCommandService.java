package com.revolut.money.transfer.api.domain.service;

import com.revolut.money.transfer.api.domain.exception.AccountAlreadyCreatedException;
import com.revolut.money.transfer.api.domain.model.Account;
import com.revolut.money.transfer.api.domain.repository.AccountRepository;

import java.util.Optional;

public class AccountCommandService {

    private final AccountRepository accountRepository;

    public AccountCommandService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void createAccount(Account account) {
        Optional<Account> exitsAccount = accountRepository.findByNumber(account.getNumber());
        if (exitsAccount.isPresent()) {
            throw new AccountAlreadyCreatedException(account.getNumber());
        } else {
            accountRepository.create(account);
        }
    }
}
