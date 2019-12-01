package com.revolut.money.transfer.api.configuration.module;

import com.revolut.money.transfer.api.domain.repository.AccountRepository;
import com.revolut.money.transfer.api.domain.repository.TransactionRepository;
import com.revolut.money.transfer.api.domain.service.AccountCommandService;
import com.revolut.money.transfer.api.domain.service.TransactionCommandService;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class CommandServiceModule {

    @Inject
    @Provides
    @Singleton
    public AccountCommandService provideAccountCommandService(AccountRepository accountRepository) {
        return new AccountCommandService(accountRepository);
    }

    @Inject
    @Provides
    @Singleton
    public TransactionCommandService provideTransactionCommandService(TransactionRepository transactionRepository,
            AccountRepository accountRepository) {
        return new TransactionCommandService(transactionRepository, accountRepository);
    }

}
