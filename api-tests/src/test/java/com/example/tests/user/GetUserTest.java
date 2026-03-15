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
public class GetUserTest extends ApiBaseTest {

    @Test
    @Story("Получение пользователя")
    public void testCreateAndGetUser() {
        logStep("1. Создаём пользователя");

        // 1. Создаём пользователя
        Map<String, Object> newUser = TestDataGenerator.generateRandomUser();
        String username = (String) newUser.get("username");

        Response createResponse = RestAssured
                .given()
                .contentType("application/json")
                .body(newUser)
                .post("/user");

        assertEquals(200, createResponse.statusCode());
        logStep("Пользователь создан с username: " + username);

        // 2. Получаем этого пользователя
        logStep("2. Получаем пользователя: " + username);

        Response getResponse = RestAssured
                .given()
                .log().all()
                .get("/user/" + username);

        assertEquals(200, getResponse.statusCode());

        String returnedUsername = getResponse.jsonPath().getString("username");
        assertEquals(username, returnedUsername);

        attachResponse(getResponse.asString());
        logStep("Пользователь успешно получен");
    }
}