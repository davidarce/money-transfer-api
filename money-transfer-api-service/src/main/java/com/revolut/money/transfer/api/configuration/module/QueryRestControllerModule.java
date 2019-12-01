package com.revolut.money.transfer.api.configuration.module;

import com.revolut.money.transfer.api.controller.account.AccountQueryRestController;
import com.revolut.money.transfer.api.controller.transaction.TransactionQueryRestController;
import com.revolut.money.transfer.api.domain.service.AccountQueryService;
import com.revolut.money.transfer.api.domain.service.TransactionQueryService;
import com.revolut.money.transfer.api.transformer.AccountTransformer;
import com.revolut.money.transfer.api.transformer.TransactionTransformer;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class QueryRestControllerModule {

    @Inject
    @Provides
    @Singleton
    AccountQueryRestController provideAccountQueryRestController(
            final AccountQueryService accountQueryService,
            final AccountTransformer accountTransformer) {
        return new AccountQueryRestController(accountQueryService, accountTransformer);
    }

    @Inject
    @Provides
    @Singleton
    TransactionQueryRestController provideTransactionQueryRestController(
            final TransactionQueryService transactionQueryService,
            final TransactionTransformer transactionTransformer) {
        return new TransactionQueryRestController(transactionQueryService, transactionTransformer);
    }

}
