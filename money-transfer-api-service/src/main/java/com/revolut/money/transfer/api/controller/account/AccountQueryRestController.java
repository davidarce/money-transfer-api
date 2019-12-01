package com.revolut.money.transfer.api.controller.account;

import com.revolut.money.transfer.api.domain.service.AccountQueryService;
import com.revolut.money.transfer.api.domain.util.ApiError;
import com.revolut.money.transfer.api.response.AccountResponse;
import com.revolut.money.transfer.api.transformer.AccountTransformer;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.*;
import org.eclipse.jetty.http.HttpStatus;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class AccountQueryRestController {

    private final AccountQueryService accountQueryService;
    private final AccountTransformer transformer;

    @Inject
    public AccountQueryRestController(AccountQueryService accountQueryService,
            AccountTransformer transformer) {
        this.accountQueryService = accountQueryService;
        this.transformer = transformer;
    }

    @OpenApi(
            method = HttpMethod.GET,
            path = "/money/transfer/accounts",
            description = "gets all accounts",
            summary = "gets all accounts",
            queryParams = {
                    @OpenApiParam(name = "site"),
                    @OpenApiParam(name = "language")
            },
            responses = {
                    @OpenApiResponse(status = "200", content = @OpenApiContent(from = AccountResponse.class))
            }
    )
    public void getAll(final Context context) {
        final String site = context.queryParam("site");
        final String language = context.queryParam("language");

        List<AccountResponse> accounts = accountQueryService.getAllAccounts().stream()
                .map(account -> transformer.toAccountResponse(account, site, language))
                .collect(Collectors.toList());

        context.status(HttpStatus.OK_200).json(accounts);
    }

    @OpenApi(
            method = HttpMethod.GET,
            path = "/money/transfer/accounts/:number",
            description = "gets account by number",
            summary = "gets account by number",
            queryParams = {
                    @OpenApiParam(name = "site"),
                    @OpenApiParam(name = "language")
            },
            responses = {
                    @OpenApiResponse(status = "200", content = @OpenApiContent(from = AccountResponse.class, isArray = true)),
                    @OpenApiResponse(status = "404", content = @OpenApiContent(from = ApiError.class))
            }
    )
    public void getByNumber(final Context context) {
        String number = context.pathParam("number");
        final String site = context.queryParam("site");
        final String language = context.queryParam("language");

        context.status(HttpStatus.OK_200).json(
                transformer.toAccountResponse(accountQueryService.findAccountByNumber(number), site, language));
    }
}
