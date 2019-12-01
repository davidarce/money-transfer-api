package com.revolut.money.transfer.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revolut.money.transfer.api.configuration.ExceptionController;
import com.revolut.money.transfer.api.configuration.RequestLogger;
import com.revolut.money.transfer.api.configuration.component.ApplicationComponent;
import com.revolut.money.transfer.api.configuration.component.DaggerApplicationComponent;
import com.revolut.money.transfer.api.controller.account.AccountCommandRestController;
import com.revolut.money.transfer.api.controller.account.AccountQueryRestController;
import com.revolut.money.transfer.api.controller.transaction.TransactionCommandRestController;
import com.revolut.money.transfer.api.controller.transaction.TransactionQueryRestController;
import com.revolut.money.transfer.api.domain.exception.RevolutException;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJson;
import io.javalin.plugin.openapi.InitialConfigurationCreator;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.jackson.JacksonToJsonMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import javax.money.UnknownCurrencyException;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Application {

    private static final String CONTEXT_PATH = "/api/money/transfer";

    public static void main(String[] args) {

        final ApplicationComponent component = DaggerApplicationComponent.create();

        final ExceptionController exceptionController = component.exceptionController();
        final AccountQueryRestController accountQueryRestController = component.accountQueryRestController();
        final AccountCommandRestController accountCommandRestController = component.accountCommandRestController();
        final TransactionQueryRestController transactionQueryRestController = component.transactionQueryRestController();
        final TransactionCommandRestController transactionCommandRestController = component.transactionCommandRestController();

        Gson gson = new GsonBuilder().create();
        JavalinJson.setFromJsonMapper(gson::fromJson);
        JavalinJson.setToJsonMapper(gson::toJson);

        Javalin app = Javalin.create(config -> {
            config.contextPath = CONTEXT_PATH;
            config.requestLogger(new RequestLogger()::log);
            config.registerPlugin(new OpenApiPlugin(new OpenApiOptions(initialConfiguration())
                    .path("/documentation")
                    .toJsonMapper(JacksonToJsonMapper.INSTANCE)
                    .activateAnnotationScanningFor("com.revolut.money.transfer.api")));
        }).start(8080);

        app.routes(() -> {
            path("/accounts", () -> {
                get(accountQueryRestController::getAll);
                post(accountCommandRestController::create);
                path(":number", () -> get(accountQueryRestController::getByNumber));
            });

            path("/transactions", () -> {
                get(transactionQueryRestController::getAll);
                post(transactionCommandRestController::create);
                path(":id", () -> get(transactionQueryRestController::getById));
            });
        });

        app.exception(Exception.class, exceptionController::handleGeneralExceptions)
                .exception(IllegalArgumentException.class, exceptionController::handleIllegalArgumentException)
                .exception(RevolutException.class, exceptionController::handleRevolutExceptions)
                .exception(UnknownCurrencyException.class, exceptionController::handleUnknownCurrencyException);

    }

    private static InitialConfigurationCreator initialConfiguration() {
        return () -> new OpenAPI().info(new Info()
                        .version("1.0")
                        .description("Revolut Money transfer API"));
    }
}
