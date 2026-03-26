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

/*
 - Базовый класс для всех API тестов.
 - Содержит общую настройку RestAssured и интеграцию с Allure.
 - Все API тесты должны наследоваться от этого класса.
*/
public class ApiBaseTest {

    protected static final String BASE_URL = "https://petstore.swagger.io/v2";

    protected static RequestSpecification requestSpec;
    protected static Logger log = LoggerFactory.getLogger(ApiBaseTest.class);

    /* Инициализация базовых настроек перед запуском всех тестов: URL, ContentType JSON
    Добавляет Allure фильтр для автоматического логирования запросов/ответов в отчет */
    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = BASE_URL;

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured()) // Все запросы попадают в Allure отчет
                .build();
    }

    // Метод для логирования шагов теста.
    @Step("{0}")
    public static void logStep(String message) {
        log.info("🟢 " + message);
    }

    // Сохраняет тело ответа во вложение Allure отчета.
    @Attachment("Response")
    public static String attachResponse(String response) {
        return response;
    }
}