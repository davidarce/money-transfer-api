package com.revolut.money.transfer.api.domain.repository;

import com.revolut.money.transfer.api.domain.fixture.TransactionFixture;
import com.revolut.money.transfer.api.domain.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class LocalTransactionRepositoryTest {

    private TransactionRepository localTransactionRepository;

    @Before
    public void setupBeforeTest() {
        localTransactionRepository = new LocalTransactionRepository();
    }

    @Test
    public void shouldGetAllTransactions_whenExists() {
        localTransactionRepository.create(TransactionFixture.buildTransaction());
        localTransactionRepository.create(TransactionFixture.buildTransaction());
        localTransactionRepository.create(TransactionFixture.buildTransaction());
        localTransactionRepository.create(TransactionFixture.buildTransaction());
        localTransactionRepository.create(TransactionFixture.buildTransaction());
        localTransactionRepository.create(TransactionFixture.buildTransaction());

        List<Transaction> transactions = localTransactionRepository.getAll();

        assertNotNull(transactions);
        assertFalse(transactions.isEmpty());
        assertEquals(6, transactions.size());
    }

    @Test
    public void shouldSaveTransaction_whenCreateValidTransaction() {
        String id = UUID.randomUUID().toString();
        localTransactionRepository.create(TransactionFixture.buildTransactionWithId(id));
        Transaction TransactionCreated = localTransactionRepository.findById(id)
                .orElse(null);

        assertNotNull(TransactionCreated);
        assertEquals(id, TransactionCreated.getId());
    }

    @Test
    public void shouldNotGetAnyTransaction_whenTransactionNotExist() {
        localTransactionRepository.create(TransactionFixture.buildTransaction());
        Transaction TransactionRetrieved = localTransactionRepository.findById("anotherId")
                .orElse(null);

        assertNull(TransactionRetrieved);
    }
}
