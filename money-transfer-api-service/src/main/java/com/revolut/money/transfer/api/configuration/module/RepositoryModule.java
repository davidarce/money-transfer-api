package com.revolut.money.transfer.api.configuration.module;

import com.revolut.money.transfer.api.domain.repository.AccountRepository;
import com.revolut.money.transfer.api.domain.repository.LocalAccountRepository;
import com.revolut.money.transfer.api.domain.repository.LocalTransactionRepository;
import com.revolut.money.transfer.api.domain.repository.TransactionRepository;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    public AccountRepository provideAccountRepository() {
        return new LocalAccountRepository();
    }

    @Provides
    @Singleton
    public TransactionRepository provideTransactionRepository() {
        return new LocalTransactionRepository();
    }
}
