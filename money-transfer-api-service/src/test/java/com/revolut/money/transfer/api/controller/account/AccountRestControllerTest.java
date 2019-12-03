package com.revolut.money.transfer.api.controller.account;

import com.revolut.money.transfer.api.controller.RevolutRestControllerTest;
import com.revolut.money.transfer.api.domain.util.ApiError;
import com.revolut.money.transfer.api.request.AccountRequest;
import com.revolut.money.transfer.api.request.MoneyRequest;
import com.revolut.money.transfer.api.response.AccountResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class AccountRestControllerTest extends RevolutRestControllerTest {

    private AccountRequest baseRequest;

    @Before
    public void setupBeforeTest() {
        baseRequest = new AccountRequest()
                .setOwner("DAVID")
                .setFunds(new MoneyRequest()
                        .setCurrency("USD")
                        .setValue("1000"));
    }

    @Test
    public void shouldCreateAnAccount_whenRequestedPostAccount_thenResponseOK() {
        given()
                .spec(spec)
                .body(baseRequest)
                .when()
                .post("/accounts")
                .then()
                .statusCode(HttpStatus.CREATED_201);
    }

    @Test
    public void shouldNotCreateAnAccount_whenRequestedIsNull_thenResponseBadRequest() {
        given()
                .spec(spec)
                .when()
                .post("/accounts")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void shouldNotCreateAnAccount_whenOwnerInformationIsEmpty_thenResponseBadRequest() {
        ApiError apiError = given()
                .spec(spec)
                .body(baseRequest.setOwner(""))
                .when()
                .post("/accounts")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .extract().as(ApiError.class);

        assertThat(apiError.getStackTrace(), containsString("Account owner could not be empty"));
    }

    @Test
    public void shouldNotCreateAnAccount_whenFundsInformationIsEmpty_thenResponseBadRequest() {
        ApiError apiError = given()
                .spec(spec)
                .body(baseRequest.setFunds(null))
                .when()
                .post("/accounts")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .extract().as(ApiError.class);

        assertThat(apiError.getStackTrace(), containsString("Account funds could not be empty"));
    }

    @Test
    public void shouldNotCreateAnAccount_whenCurrencyIsEmpty_thenResponseBadRequest() {
        ApiError apiError = given()
                .spec(spec)
                .body(baseRequest.setFunds(new MoneyRequest()
                        .setCurrency("")
                        .setValue("1000")))
                .when()
                .post("/accounts")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .extract().as(ApiError.class);

        assertThat(apiError.getStackTrace(), containsString("Currency funds could not be empty"));
    }

    @Test
    public void shouldNotCreateAnAccount_whenAmountFundsIsEmpty_thenResponseBadRequest() {
        ApiError apiError = given()
                .spec(spec)
                .body(baseRequest.setFunds(new MoneyRequest()
                        .setCurrency("USD")
                        .setValue("")))
                .when()
                .post("/accounts")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .extract().as(ApiError.class);

        assertThat(apiError.getStackTrace(), containsString("Amount funds could not be empty"));
    }

    @Test
    public void shouldNotCreateAnAccount_whenCurrencyIsNotValid_thenResponseBadRequest() {
        ApiError apiError = given()
                .spec(spec)
                .body(baseRequest.setFunds(new MoneyRequest()
                        .setCurrency("REVOLUT")
                        .setValue("1000")))
                .when()
                .post("/accounts")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .extract().as(ApiError.class);

        assertThat(apiError.getStackTrace(), containsString("UnknownCurrencyException"));
    }

    @Test
    public void shouldNotCreateAnAccount_whenAmountFundsIsNegative_thenResponseBadRequest() {
        ApiError apiError = given()
                .spec(spec)
                .body(baseRequest.setFunds(new MoneyRequest()
                        .setCurrency("USD")
                        .setValue("-1000")))
                .when()
                .post("/accounts")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .extract().as(ApiError.class);

        assertThat(apiError.getStackTrace(), containsString("Money could not be negative"));
    }

    @Test
    public void shouldGetAllAccounts_whenRequestedGetAllAccounts_thenResponseOK() {
        AccountResponse accountResponse = given()
                .spec(spec)
                .body(baseRequest)
                .when()
                .post("/accounts")
                .then()
                .statusCode(HttpStatus.CREATED_201)
                .extract().as(AccountResponse.class);

        given().spec(spec)
                .get("/accounts")
                .then()
                .assertThat().body(containsString(accountResponse.getNumber()));

    }

    @Test
    public void shouldGetAnAccount_whenRequestedGetWithNumberAccount_thenResponseOK() {
        AccountResponse accountResponse = given()
                .spec(spec)
                .body(baseRequest)
                .when()
                .post("/accounts")
                .then()
                .statusCode(HttpStatus.CREATED_201)
                .extract().as(AccountResponse.class);

        AccountResponse accountCreated = given().spec(spec)
                .pathParam("number", accountResponse.getNumber())
                .get("/accounts/{number}")
                .then()
                .statusCode(HttpStatus.OK_200)
                .extract().as(AccountResponse.class);

        assertThat(accountCreated, hasProperty("number", equalTo(accountResponse.getNumber())));
    }

    @Test
    public void shouldNotGetAnAccount_whenRequestedGetWithNumberAccountNotExits_thenResponseNotFound() {
        ApiError apiError = given().spec(spec)
                .pathParam("number", "00000000-0000-0000-0000-000000000000")
                .get("/accounts/{number}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND_404)
                .extract().as(ApiError.class);

        assertThat(apiError.getStackTrace(),
                containsString("Account with number 00000000-0000-0000-0000-000000000000 not found!"));

    }
}
