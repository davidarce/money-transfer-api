package com.revolut.money.transfer.api.configuration.module;

import com.revolut.money.transfer.api.configuration.ExceptionController;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class ExceptionControllerModule {

    @Provides
    @Singleton ExceptionController provideExceptionController() {
        return new ExceptionController();
    }
}
