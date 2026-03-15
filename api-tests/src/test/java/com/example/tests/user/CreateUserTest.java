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
public class CreateUserTest extends ApiBaseTest {

    @Test
    @Story("Создание пользователя")
    public void testCreateUser() {
        logStep("Создание нового пользователя");

        Map<String, Object> newUser = TestDataGenerator.generateRandomUser();
        Response response = RestAssured
                .given()
                .log().all()
                .contentType("application/json")
                .body(newUser)
                .post("/user");

        assertEquals(200, response.statusCode());

        attachResponse(response.asString());
        logStep("Пользователь создан");
    }
}