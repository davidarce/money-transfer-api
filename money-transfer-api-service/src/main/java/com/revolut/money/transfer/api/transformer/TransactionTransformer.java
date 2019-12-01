package com.revolut.money.transfer.api.transformer;

import com.google.common.base.Preconditions;
import com.revolut.money.transfer.api.domain.model.Transaction;
import com.revolut.money.transfer.api.domain.util.DateUtils;
import com.revolut.money.transfer.api.request.MoneyRequest;
import com.revolut.money.transfer.api.request.TransactionRequest;
import com.revolut.money.transfer.api.response.TransactionResponse;
import org.apache.commons.lang3.StringUtils;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class TransactionTransformer {

    public Transaction toTransactionDomain(TransactionRequest request) {
        Preconditions.checkArgument(Objects.nonNull(request), "transactionRequest information could not be empty");
        Preconditions.checkArgument(!StringUtils.isBlank(request.getOrigin()), "Origin account number could not be empty");
        Preconditions.checkArgument(!StringUtils.isBlank(request.getDestination()), "Destination account number could not be empty");
        Preconditions.checkArgument(!StringUtils.equals(request.getOrigin(), request.getDestination()), "Origin and destination account could not be the same");
        Preconditions.checkArgument(Objects.nonNull(request.getAmount()), "Amount could not be empty");
        Preconditions.checkArgument(!StringUtils.isBlank(request.getAmount().getCurrency()), "Currency amount could not be empty");
        Preconditions.checkArgument(!StringUtils.isBlank(request.getAmount().getValue()), "Transaction amount value could not be empty");

        return Transaction.builder()
                .withId(UUID.randomUUID().toString())
                .withOrigin(request.getOrigin())
                .withDestination(request.getDestination())
                .withAmount(toMonetaryAmount(request.getAmount()))
                .build();
    }

    public TransactionResponse toTransactionResponse(Transaction transaction, String site, String language) {
        Locale locale = new Locale(language, site);
        MonetaryAmountFormat monetaryAmountFormat = MonetaryFormats.getAmountFormat(locale);
        String amount = monetaryAmountFormat.format(transaction.getAmount());

        return new TransactionResponse(transaction.getId(), transaction.getOrigin(),
                transaction.getDestination(), amount, transaction.getStatus().toString(),
                DateUtils.localDateTimeToString(transaction.getCreatedAt()));
    }

    private MonetaryAmount toMonetaryAmount(MoneyRequest moneyRequest) {
        return Money.of(new BigDecimal(moneyRequest.getValue()), moneyRequest.getCurrency());
    }
}
