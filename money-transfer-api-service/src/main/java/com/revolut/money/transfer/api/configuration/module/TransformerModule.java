package com.revolut.money.transfer.api.configuration.module;

import com.revolut.money.transfer.api.transformer.AccountTransformer;
import com.revolut.money.transfer.api.transformer.TransactionTransformer;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class TransformerModule {

    @Provides
    @Singleton
    AccountTransformer provideAccountTransformer() {
        return new AccountTransformer();
    }

    @Provides
    @Singleton
    TransactionTransformer provideTransactionTransformer() {
        return new TransactionTransformer();
    }
}
