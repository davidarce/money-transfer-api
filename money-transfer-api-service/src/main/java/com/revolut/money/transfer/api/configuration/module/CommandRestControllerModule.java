package com.revolut.money.transfer.api.configuration.module;

import com.revolut.money.transfer.api.controller.account.AccountCommandRestController;
import com.revolut.money.transfer.api.controller.account.AccountQueryRestController;
import com.revolut.money.transfer.api.controller.transaction.TransactionCommandRestController;
import com.revolut.money.transfer.api.controller.transaction.TransactionQueryRestController;
import com.revolut.money.transfer.api.domain.service.AccountCommandService;
import com.revolut.money.transfer.api.domain.service.AccountQueryService;
import com.revolut.money.transfer.api.domain.service.TransactionCommandService;
import com.revolut.money.transfer.api.domain.service.TransactionQueryService;
import com.revolut.money.transfer.api.transformer.AccountTransformer;
import com.revolut.money.transfer.api.transformer.TransactionTransformer;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class CommandRestControllerModule {

    @Inject
    @Provides
    @Singleton
    AccountCommandRestController provideAccountCommandRestController(
            final AccountCommandService accountCommandService,
            final AccountTransformer accountTransformer) {
        return new AccountCommandRestController(accountCommandService, accountTransformer);
    }

    @Inject
    @Provides
    @Singleton
    TransactionCommandRestController provideTransactionCommandRestController(
            final TransactionCommandService transactionCommandService,
            final TransactionTransformer transactionTransformer) {
        return new TransactionCommandRestController(transactionCommandService, transactionTransformer);
    }
}
