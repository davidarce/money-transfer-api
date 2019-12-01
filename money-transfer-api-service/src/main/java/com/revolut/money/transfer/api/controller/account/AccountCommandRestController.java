package com.revolut.money.transfer.api.controller.account;

import com.revolut.money.transfer.api.domain.model.Account;
import com.revolut.money.transfer.api.domain.service.AccountCommandService;
import com.revolut.money.transfer.api.domain.util.ApiError;
import com.revolut.money.transfer.api.request.AccountRequest;
import com.revolut.money.transfer.api.response.AccountResponse;
import com.revolut.money.transfer.api.transformer.AccountTransformer;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.*;
import org.eclipse.jetty.http.HttpStatus;

public class AccountCommandRestController {

    private final AccountCommandService accountCommandService;
    private final AccountTransformer transformer;

    public AccountCommandRestController(AccountCommandService accountCommandService,
            AccountTransformer transformer) {
        this.accountCommandService = accountCommandService;
        this.transformer = transformer;
    }

    @OpenApi(
            path = "/money/transfer/accounts",
            description = "create an account",
            summary = "create an account",
            queryParams = {
                    @OpenApiParam(name = "site"),
                    @OpenApiParam(name = "language")
            },
            requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = AccountRequest.class)),
            responses = {
                    @OpenApiResponse(status = "201", content = @OpenApiContent(from = AccountResponse.class)),
                    @OpenApiResponse(status = "400", content = @OpenApiContent(from = ApiError.class))
            }
    )
    public void create(final Context context) {
        final AccountRequest accountRequest = context.bodyAsClass(AccountRequest.class);
        final String site = context.queryParam("site");
        final String language = context.queryParam("language");

        Account accountCreated = transformer.toAccountDomain(accountRequest);

        accountCommandService.createAccount(accountCreated);
        context.status(HttpStatus.OK_200).json(
                transformer.toAccountResponse(accountCreated, site, language));
    }
}
