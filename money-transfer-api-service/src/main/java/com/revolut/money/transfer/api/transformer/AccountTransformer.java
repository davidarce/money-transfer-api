package com.revolut.money.transfer.api.transformer;

import com.google.common.base.Preconditions;
import com.revolut.money.transfer.api.domain.model.Account;
import com.revolut.money.transfer.api.domain.util.DateUtils;
import com.revolut.money.transfer.api.request.AccountRequest;
import com.revolut.money.transfer.api.request.MoneyRequest;
import com.revolut.money.transfer.api.response.AccountResponse;
import org.apache.commons.lang3.StringUtils;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class AccountTransformer {

    public Account toAccountDomain(AccountRequest accountRequest) {
        Preconditions.checkArgument(Objects.nonNull(accountRequest), "Account information could not be empty");
        Preconditions.checkArgument(!StringUtils.isBlank(accountRequest.getOwner()), "Account owner could not be empty");
        Preconditions.checkArgument(Objects.nonNull(accountRequest.getFunds()), "Account funds could not be empty");
        Preconditions.checkArgument(!StringUtils.isBlank(accountRequest.getFunds().getCurrency()), "Currency funds could not be empty");
        Preconditions.checkArgument(!StringUtils.isBlank(accountRequest.getFunds().getValue()), "Amount funds could not be empty");

        return Account.builder()
                .withNumber(UUID.randomUUID().toString())
                .withOwner(accountRequest.getOwner())
                .withFunds(toMonetaryAmount(accountRequest.getFunds()))
                .createdAt(LocalDateTime.now())
                .build();
    }

    public AccountResponse toAccountResponse(Account account, String site, String language) {
        Locale locale = new Locale(language, site);
        MonetaryAmountFormat monetaryAmountFormat = MonetaryFormats.getAmountFormat(locale);
        String funds = monetaryAmountFormat.format(account.getFunds());

        return new AccountResponse(account.getNumber(), account.getOwner(),
                DateUtils.localDateTimeToString(account.getCreatedAt()), funds);
    }

    private MonetaryAmount toMonetaryAmount(MoneyRequest moneyRequest) {
        return Money.of(new BigDecimal(moneyRequest.getValue()), moneyRequest.getCurrency());
    }
}
