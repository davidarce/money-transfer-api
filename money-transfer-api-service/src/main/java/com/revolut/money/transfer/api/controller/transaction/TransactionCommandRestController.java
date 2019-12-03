package com.revolut.money.transfer.api.controller.transaction;

import com.revolut.money.transfer.api.domain.model.Transaction;
import com.revolut.money.transfer.api.domain.service.TransactionCommandService;
import com.revolut.money.transfer.api.domain.util.ApiError;
import com.revolut.money.transfer.api.request.TransactionRequest;
import com.revolut.money.transfer.api.response.TransactionResponse;
import com.revolut.money.transfer.api.transformer.TransactionTransformer;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.*;
import org.eclipse.jetty.http.HttpStatus;

public class TransactionCommandRestController {

    private final TransactionCommandService transactionCommandService;
    private final TransactionTransformer transformer;

    public TransactionCommandRestController(TransactionCommandService transactionCommandService,
            TransactionTransformer transformer) {
        this.transactionCommandService = transactionCommandService;
        this.transformer = transformer;
    }

    @OpenApi(
            path = "/money/transfer/transactions",
            description = "create a transaction",
            summary = "create a transaction between accounts",
            queryParams = {
                    @OpenApiParam(name = "site"),
                    @OpenApiParam(name = "language")
            },
            requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = TransactionRequest.class)),
            responses = {
                    @OpenApiResponse(status = "201", content = @OpenApiContent(from = TransactionResponse.class)),
                    @OpenApiResponse(status = "400", content = @OpenApiContent(from = ApiError.class))
            }
    )
    public void create(final Context context) {
        final String site = context.queryParam("site");
        final String language = context.queryParam("language");
        TransactionRequest transactionRequest = context.bodyAsClass(TransactionRequest.class);

        Transaction transaction = transformer.toTransactionDomain(transactionRequest);
        transactionCommandService.createTransaction(transaction);

        context.status(HttpStatus.CREATED_201).json(
                transformer.toTransactionResponse(transaction, site, language));
    }
}
