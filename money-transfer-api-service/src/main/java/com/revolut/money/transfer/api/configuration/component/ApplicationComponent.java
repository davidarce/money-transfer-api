package com.revolut.money.transfer.api.configuration.component;

import com.revolut.money.transfer.api.configuration.ExceptionController;
import com.revolut.money.transfer.api.configuration.module.*;
import com.revolut.money.transfer.api.controller.account.AccountCommandRestController;
import com.revolut.money.transfer.api.controller.account.AccountQueryRestController;
import com.revolut.money.transfer.api.controller.transaction.TransactionCommandRestController;
import com.revolut.money.transfer.api.controller.transaction.TransactionQueryRestController;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        QueryRestControllerModule.class,
        CommandRestControllerModule.class,
        ExceptionControllerModule.class,
        QueryServiceModule.class,
        CommandServiceModule.class,
        RepositoryModule.class,
        TransformerModule.class
})
public interface ApplicationComponent {

    AccountQueryRestController accountQueryRestController();

    TransactionQueryRestController transactionQueryRestController();

    AccountCommandRestController accountCommandRestController();

    TransactionCommandRestController transactionCommandRestController();

    ExceptionController exceptionController();

}
