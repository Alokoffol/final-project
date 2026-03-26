package com.example.tests.user;

import com.example.base.ApiBaseTest;
import com.example.utils.TestDataGenerator;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

@Feature("User")
public class LoginLogoutTest extends ApiBaseTest {

    @Test
    @Story("Вход и выход с учетной записи")
    public void testLoginAndLogout() {
        logStep("1. Создаём пользователя");

        // Создаём пользователя
        Map<String, Object> newUser = TestDataGenerator.generateRandomUser();
        String username = (String) newUser.get("username");
        String password = (String) newUser.get("password");

        Response createResponse = RestAssured
                .given()
                .contentType("application/json")
                .body(newUser)
                .post("/user");

        assertEquals(200, createResponse.statusCode());
        logStep("Пользователь создан: " + username);

        // Логинимся
        logStep("2. Логин в систему");

        Response loginResponse = RestAssured
                .given()
                .log().all()
                .queryParam("username", username)
                .queryParam("password", password)
                .when()
                .get("/user/login")
                .then()
                .log().all()
                .extract().response();

        assertEquals(200, loginResponse.statusCode());

        // Проверяем, что пришёл ответ с сообщением
        String message = loginResponse.jsonPath().getString("message");
        assertNotNull("Сообщение не должно быть null", message);
        logStep("Ответ сервера: " + message);

        // Извлекаем куку/токен, если есть
        String sessionId = loginResponse.getCookie("JSESSIONID");
        if (sessionId != null) {
            logStep("Получена сессия: " + sessionId);
        }

        // Логаут
        logStep("3. Логаут из системы");

        Response logoutResponse = RestAssured
                .given()
                .log().all()
                .when()
                .get("/user/logout")
                .then()
                .log().all()
                .extract().response();

        assertEquals(200, logoutResponse.statusCode());

        attachResponse(logoutResponse.asString());
        logStep("Логаут выполнен успешно");
    }
}