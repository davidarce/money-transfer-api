package com.revolut.money.transfer.api.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.revolut.money.transfer.api.Application;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class RevolutRestControllerTest {

    private static Javalin app;
    protected static RequestSpecification spec;

    @BeforeClass
    public static void setup() {
        spec = new RequestSpecBuilder()
                .addQueryParam("site", "USA")
                .addQueryParam("language", "en")
                .setBasePath("api/money/transfer")
                .build();

        configureObjectMapper();
        startServer();
    }

    private static void configureObjectMapper() {
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
                new ObjectMapperConfig().jackson2ObjectMapperFactory((cls, charset) -> {
                            ObjectMapper objectMapper = new ObjectMapper();
                            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                            objectMapper.registerModule(new JavaTimeModule());
                            return objectMapper;
                        }
                ));
    }

    private static void startServer() {
        app = Application.JavalinApp.init();
    }

    @AfterClass
    public static void stopServer() {
        app.stop();
    }
}
