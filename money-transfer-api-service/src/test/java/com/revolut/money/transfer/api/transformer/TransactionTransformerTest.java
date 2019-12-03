package com.revolut.money.transfer.api.transformer;

import com.revolut.money.transfer.api.domain.model.Transaction;
import com.revolut.money.transfer.api.domain.util.TransactionStatus;
import com.revolut.money.transfer.api.request.MoneyRequest;
import com.revolut.money.transfer.api.request.TransactionRequest;
import com.revolut.money.transfer.api.response.TransactionResponse;
import org.javamoney.moneta.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionTransformerTest {

    private TransactionTransformer transactionTransformer = new TransactionTransformer();

    @Test
    public void shouldTransformTransactionRequestToATransactionDomainValid() {
        TransactionRequest request = new TransactionRequest()
                .setOrigin("aAccountOrigin")
                .setDestination("aAccountDestination")
                .setAmount(new MoneyRequest()
                        .setValue("1000")
                        .setCurrency("USD"));

        Transaction transaction = transactionTransformer.toTransactionDomain(request);

        assertNotNull(transaction);
        assertNotNull(transaction.getId());
        assertThat(transaction, hasProperty("status", equalTo(TransactionStatus.PROCESSING)));
        assertThat(transaction, hasProperty("amount", equalTo(Money.of(1000, "USD"))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreatedTransactionDomain_whenTransactionRequestIsNotValid() {
        transactionTransformer.toTransactionDomain(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreatedTransactionDomain_whenTransactionAmountIsNotValid() {
        TransactionRequest request = new TransactionRequest()
                .setOrigin("aAccountOrigin")
                .setDestination("aAccountDestination")
                .setAmount(null);

        transactionTransformer.toTransactionDomain(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreatedTransactionDomain_whenOriginAccountIsNotValid() {
        TransactionRequest request = new TransactionRequest()
                .setOrigin("")
                .setDestination("aAccountDestination")
                .setAmount(new MoneyRequest()
                        .setValue("1000")
                        .setCurrency("USD"));

        transactionTransformer.toTransactionDomain(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreatedTransactionDomain_whenDestinationAccountIsNotValid() {
        TransactionRequest request = new TransactionRequest()
                .setOrigin("aAccountOrigin")
                .setDestination("")
                .setAmount(new MoneyRequest()
                        .setValue("1000")
                        .setCurrency("USD"));

        transactionTransformer.toTransactionDomain(request);
    }

    @Test
    public void shouldTransformTransactionDomainToTransactionResponseValid() {
        Transaction transaction = Transaction.builder()
                .withId("idTransaction")
                .withOrigin("aAccountOrigin")
                .withDestination("aAccountDestination")
                .withAmount(Money.of(1000, "USD"))
                .build();

        TransactionResponse response = transactionTransformer.toTransactionResponse(
                transaction, "USA", "en");

        assertNotNull(response);
        assertEquals("idTransaction", response.getId());
        assertEquals("aAccountOrigin", response.getOrigin());
        assertEquals("aAccountDestination", response.getDestination());
        assertThat(response, hasProperty("amount", equalTo("USD1,000.00")));
    }
}
