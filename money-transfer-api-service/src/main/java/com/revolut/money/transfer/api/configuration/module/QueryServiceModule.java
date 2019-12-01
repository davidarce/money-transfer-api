package com.revolut.money.transfer.api.configuration.module;

import com.revolut.money.transfer.api.domain.repository.AccountRepository;
import com.revolut.money.transfer.api.domain.repository.TransactionRepository;
import com.revolut.money.transfer.api.domain.service.AccountQueryService;
import com.revolut.money.transfer.api.domain.service.TransactionQueryService;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class QueryServiceModule {

    @Inject
    @Provides
    @Singleton
    public AccountQueryService provideAccountQueryService(AccountRepository accountRepository) {
        return new AccountQueryService(accountRepository);
    }

    @Inject
    @Provides
    @Singleton
    public TransactionQueryService provideTransactionQueryService(TransactionRepository transactionRepository) {
        return new TransactionQueryService(transactionRepository);
    }

}
