package com.revolut.money.transfer.api.controller.transaction;

import com.revolut.money.transfer.api.domain.service.TransactionQueryService;
import com.revolut.money.transfer.api.domain.util.ApiError;
import com.revolut.money.transfer.api.response.TransactionResponse;
import com.revolut.money.transfer.api.transformer.TransactionTransformer;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.*;
import org.eclipse.jetty.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionQueryRestController {

    private final TransactionQueryService transactionQueryService;
    private final TransactionTransformer transformer;

    public TransactionQueryRestController(TransactionQueryService transactionQueryService,
            TransactionTransformer transformer) {
        this.transactionQueryService = transactionQueryService;
        this.transformer = transformer;
    }

    @OpenApi(
            method = HttpMethod.GET,
            path = "/money/transfer/transactions",
            description = "gets all transactions",
            summary = "get all transactions",
            queryParams = {
                    @OpenApiParam(name = "site"),
                    @OpenApiParam(name = "language")
            },
            responses = {
                    @OpenApiResponse(status = "200", content = @OpenApiContent(from = TransactionResponse.class))
            }
    )
    public void getAll(final Context context) {
        final String site = context.queryParam("site");
        final String language = context.queryParam("language");

        List<TransactionResponse> transactions = transactionQueryService.getAllTransactions().stream()
                .map(transaction -> transformer.toTransactionResponse(transaction, site, language))
                .collect(Collectors.toList());

        context.status(HttpStatus.OK_200).json(transactions);
    }

    @OpenApi(
            method = HttpMethod.GET,
            path = "/money/transfer/transactions/:id",
            description = "gets transaction by id",
            summary = "gets a transaction by id",
            queryParams = {
                    @OpenApiParam(name = "site"),
                    @OpenApiParam(name = "language")
            },
            responses = {
                    @OpenApiResponse(status = "200", content = @OpenApiContent(from = TransactionResponse.class, isArray = true)),
                    @OpenApiResponse(status = "404", content = @OpenApiContent(from = ApiError.class))
            }
    )
    public void getById(final Context context) {
        final String id = context.pathParam("id");
        final String site = context.queryParam("site");
        final String language = context.queryParam("language");
        context.status(HttpStatus.OK_200).json(transformer.toTransactionResponse(
                        transactionQueryService.findTransactionById(id), site, language));
    }
}
