package com.revolut.money.transfer.api.controller.transaction;

import com.revolut.money.transfer.api.controller.RevolutRestControllerTest;
import com.revolut.money.transfer.api.domain.util.ApiError;
import com.revolut.money.transfer.api.request.AccountRequest;
import com.revolut.money.transfer.api.request.MoneyRequest;
import com.revolut.money.transfer.api.request.TransactionRequest;
import com.revolut.money.transfer.api.response.AccountResponse;
import com.revolut.money.transfer.api.response.TransactionResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TransactionRestControllerTest extends RevolutRestControllerTest {

    private TransactionRequest baseRequest;
    private ExecutorService executorService = Executors.newScheduledThreadPool(10);

    @Before
    public void setupBeforeTest() {
        AccountRequest anAccount = new AccountRequest()
                .setOwner("REVOLUT")
                .setFunds(new MoneyRequest()
                        .setCurrency("USD")
                        .setValue("1000"));

        AccountRequest anotherAccount = new AccountRequest()
                .setOwner("DAVID")
                .setFunds(new MoneyRequest()
                        .setCurrency("USD")
                        .setValue("1000"));

        AccountResponse anAccountCreated = given()
                .spec(spec)
                .body(anAccount)
                .when()
                .post("/accounts")
                .then()
                .extract().as(AccountResponse.class);

        AccountResponse anotherAccountCreated = given()
                .spec(spec)
                .body(anotherAccount)
                .when()
                .post("/accounts")
                .then()
                .extract().as(AccountResponse.class);

        baseRequest = new TransactionRequest()
                .setOrigin(anAccountCreated.getNumber())
                .setDestination(anotherAccountCreated.getNumber());

    }

    @Test
    public void shouldCreateATransactionBetweenAccounts_whenRequestedPostTransaction_thenResponseOK() {
        TransactionResponse transactionResponse = given()
                .spec(spec)
                .body(baseRequest.setAmount(
                        new MoneyRequest()
                                .setCurrency("USD")
                                .setValue("100")
                ))
                .when()
                .post("/transactions")
                .then()
                .statusCode(HttpStatus.CREATED_201)
                .extract().as(TransactionResponse.class);

        AccountResponse originAccount = given()
                .spec(spec)
                .pathParam("number", transactionResponse.getOrigin())
                .get("/accounts/{number}")
                .then()
                .extract().as(AccountResponse.class);

        AccountResponse destinationAccount = given()
                .spec(spec)
                .pathParam("number", transactionResponse.getDestination())
                .get("/accounts/{number}")
                .then()
                .extract().as(AccountResponse.class);

        assertThat(originAccount, hasProperty("funds", equalTo("USD900.00")));
        assertThat(destinationAccount, hasProperty("funds", equalTo("USD1,100.00")));
    }

    @Test
    public void shouldNotCreateATransactionBetweenAccounts_whenOriginAccountNotValid_thenResponseNotFound() {
        ApiError apiError = given()
                .spec(spec)
                .body(baseRequest.setOrigin("invalidAccount")
                        .setAmount(new MoneyRequest()
                                .setCurrency("USD")
                                .setValue("100")))
                .when()
                .post("/transactions")
                .then()
                .statusCode(HttpStatus.NOT_FOUND_404)
                .extract().as(ApiError.class);

        assertThat(apiError,
                hasProperty("stackTrace", containsString("Account with number invalidAccount not found!")));
    }

    @Test
    public void shouldNotCreateATransactionBetweenAccounts_whenDestinationAccountNotValid_thenResponseNotFound() {
        ApiError apiError = given()
                .spec(spec)
                .body(baseRequest.setDestination("invalidAccount")
                        .setAmount(new MoneyRequest()
                                .setCurrency("USD")
                                .setValue("100")))
                .when()
                .post("/transactions")
                .then()
                .statusCode(HttpStatus.NOT_FOUND_404)
                .extract().as(ApiError.class);

        assertThat(apiError,
                hasProperty("stackTrace", containsString("Account with number invalidAccount not found!")));
    }

    @Test
    public void shouldNotCreateATransactionBetweenAccounts_whenAmountIsEmpty_thenResponseBadRequest() {
        ApiError apiError = given()
                .spec(spec)
                .body(baseRequest.setAmount(null))
                .when()
                .post("/transactions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .extract().as(ApiError.class);

        assertThat(apiError, hasProperty("stackTrace", containsString("Amount could not be empty")));
    }

    @Test
    public void shouldNotCreateATransactionBetweenAccounts_whenAmountNegativeOrZero_thenResponseBadRequest() {
        ApiError apiError = given()
                .spec(spec)
                .body(baseRequest.setAmount(new MoneyRequest()
                        .setCurrency("USD")
                        .setValue("0")))
                .when()
                .post("/transactions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .extract().as(ApiError.class);

        assertThat(apiError, hasProperty("stackTrace", containsString("Amount could not be negative or Zero")));
    }

    @Test
    public void shouldNotCreateATransactionBetweenAccounts_whenAmountValueIsEmpty_thenResponseBadRequest() {
        ApiError apiError = given()
                .spec(spec)
                .body(baseRequest.setAmount(new MoneyRequest()
                        .setCurrency("USD")
                        .setValue(null)))
                .when()
                .post("/transactions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .extract().as(ApiError.class);

        assertThat(apiError, hasProperty("stackTrace", containsString("Transaction amount value could not be empty")));
    }

    @Test
    public void shouldNotCreateATransactionBetweenAccounts_whenCurrencyAmountIsEmpty_thenResponseBadRequest() {
        ApiError apiError = given()
                .spec(spec)
                .body(baseRequest.setAmount(new MoneyRequest()
                        .setCurrency(null)
                        .setValue("100")))
                .when()
                .post("/transactions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .extract().as(ApiError.class);

        assertThat(apiError, hasProperty("stackTrace", containsString("Currency amount could not be empty")));
    }

    @Test
    public void shouldNotCreateATransactionBetweenAccounts_whenCurrencyIsNotValid_thenResponseBadRequest() {
        ApiError apiError = given()
                .spec(spec)
                .body(baseRequest.setAmount(new MoneyRequest()
                        .setCurrency("REVOLUT")
                        .setValue("100")))
                .when()
                .post("/transactions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .extract().as(ApiError.class);

        assertThat(apiError, hasProperty("stackTrace", containsString("UnknownCurrencyException")));
    }

    @Test
    public void shouldNotCreateATransactionBetweenAccounts_whenAccountOriginHaveDifferentCurrency_thenResponseBadRequest() {
        ApiError apiError = given()
                .spec(spec)
                .body(baseRequest.setAmount(new MoneyRequest()
                        .setCurrency("COP")
                        .setValue("500000")))
                .when()
                .post("/transactions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .extract().as(ApiError.class);

        assertThat(apiError,
                hasProperty("message", containsString("Account origin and transaction have different currencies")));
    }

    @Test
    public void shouldNotCreateATransactionBetweenAccounts_whenAccountOriginDoesNotHaveFunds_thenResponseBadRequest() {
        ApiError apiError = given()
                .spec(spec)
                .body(baseRequest.setAmount(new MoneyRequest()
                        .setCurrency("USD")
                        .setValue("5000")))
                .when()
                .post("/transactions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .extract().as(ApiError.class);

        assertThat(apiError, hasProperty("message", containsString("does not have sufficient funds")));
    }

    @Test
    public void shouldNotCreateATransactionBetweenAccounts_whenAccountsHaveDifferentCurrencies_thenResponseBadRequest() {
        AccountRequest homeroAccount = new AccountRequest()
                .setOwner("HOMERO")
                .setFunds(new MoneyRequest()
                        .setCurrency("COP")
                        .setValue("1000000"));

        AccountResponse homeroAccountCreated = given()
                .spec(spec)
                .body(homeroAccount)
                .when()
                .post("/accounts")
                .then()
                .extract().as(AccountResponse.class);

        ApiError apiError = given()
                .spec(spec)
                .body(baseRequest.setDestination(homeroAccountCreated.getNumber())
                        .setAmount(new MoneyRequest()
                                .setCurrency("USD")
                                .setValue("100")))
                .when()
                .post("/transactions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .extract().as(ApiError.class);

        assertThat(apiError, hasProperty("message", containsString("Accounts have different currencies")));
    }

    @Test
    public void shouldGetTransaction_whenRequestedTransactionInformation_thenResponseOK() {
        TransactionResponse transactionResponse = given()
                .spec(spec)
                .body(baseRequest.setAmount(
                        new MoneyRequest()
                                .setCurrency("USD")
                                .setValue("100")
                ))
                .when()
                .post("/transactions")
                .then()
                .statusCode(HttpStatus.CREATED_201)
                .extract().as(TransactionResponse.class);

        TransactionResponse transactionInformation = given()
                .spec(spec)
                .pathParam("id", transactionResponse.getId())
                .get("/transactions/{id}")
                .then()
                .extract().as(TransactionResponse.class);

        assertThat(transactionInformation, hasProperty("status", equalTo("CREATED")));
    }

    /*
        Multithreading scenarios
     */

    @Test
    public void shouldCreateTransactionsBetweenAccounts_whenIsRequestedMultipleTimes()
            throws InterruptedException {

        executorService.execute(this::createTransaction);
        executorService.execute(this::createTransaction);
        executorService.execute(this::createTransaction);
        executorService.execute(this::createTransaction);
        executorService.execute(this::createTransaction);
        executorService.execute(this::createTransaction);
        executorService.execute(this::createTransaction);
        executorService.execute(this::createTransaction);
        executorService.execute(this::createTransaction);
        executorService.execute(this::createTransaction);

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        AccountResponse originAccount = given()
                .spec(spec)
                .pathParam("number", baseRequest.getOrigin())
                .get("/accounts/{number}")
                .then()
                .extract().as(AccountResponse.class);

        AccountResponse destinationAccount = given()
                .spec(spec)
                .pathParam("number", baseRequest.getDestination())
                .get("/accounts/{number}")
                .then()
                .extract().as(AccountResponse.class);

        assertThat(originAccount, hasProperty("funds", equalTo("USD0.00")));
        assertThat(destinationAccount, hasProperty("funds", equalTo("USD2,000.00")));
    }

    @Test
    public void shouldCreatedOneValidTransaction_whenRequestedMultipleTimes_andOriginAccountDoesNotHaveMoreFunds()
            throws InterruptedException {

        executorService.execute(this::createTransaction);
        executorService.execute(this::createTransaction);
        executorService.execute(this::createTransaction);
        executorService.execute(this::createTransaction);
        executorService.execute(this::createTransaction);
        executorService.execute(this::createTransaction);
        executorService.execute(this::createTransaction);
        executorService.execute(this::createTransaction);
        executorService.execute(this::createTransaction);
        executorService.execute(this::createTransaction);

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        AccountResponse originAccount = given()
                .spec(spec)
                .pathParam("number", baseRequest.getOrigin())
                .get("/accounts/{number}")
                .then()
                .extract().as(AccountResponse.class);

        AccountResponse destinationAccount = given()
                .spec(spec)
                .pathParam("number", baseRequest.getDestination())
                .get("/accounts/{number}")
                .then()
                .extract().as(AccountResponse.class);

        assertThat(originAccount, hasProperty("funds", equalTo("USD0.00")));
        assertThat(destinationAccount, hasProperty("funds", equalTo("USD2,000.00")));
    }

    private void createTransaction() {
        given()
                .spec(spec)
                .body(baseRequest.setAmount(
                        new MoneyRequest()
                                .setCurrency("USD")
                                .setValue("100")
                ))
                .when()
                .post("/transactions")
                .then()
                .statusCode(HttpStatus.CREATED_201)
                .extract().as(TransactionResponse.class);

    }
}
