package com.example.base;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiBaseTest {

    protected static final String BASE_URL = "https://petstore.swagger.io/v2";

    protected static RequestSpecification requestSpec;
    protected static Logger log = LoggerFactory.getLogger(ApiBaseTest.class);

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = BASE_URL;

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }

    @Step("{0}")
    public static void logStep(String message) {
        log.info("🟢 " + message);
    }

    @Attachment("Response")
    public static String attachResponse(String response) {
        return response;
    }
}