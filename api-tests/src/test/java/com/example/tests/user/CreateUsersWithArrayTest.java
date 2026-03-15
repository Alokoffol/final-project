package com.example.tests.user;

import com.example.base.ApiBaseTest;
import com.example.utils.TestDataGenerator;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@Feature("User")
public class CreateUsersWithArrayTest extends ApiBaseTest {

    @Test
    @Story("Создание списка пользователей")
    public void testCreateUsersWithArray() {
        logStep("Создание списка пользователей через массив");

        // Создаём двух пользователей через генератор
        Map<String, Object> user1 = TestDataGenerator.generateRandomUser();
        Map<String, Object> user2 = TestDataGenerator.generateRandomUser();

        List<Map<String, Object>> users = Arrays.asList(user1, user2);

        Response response = RestAssured
                .given()
                .log().all()
                .contentType("application/json")
                .body(users)
                .when()
                .post("/user/createWithArray")
                .then()
                .log().all()
                .extract().response();

        assertEquals(200, response.statusCode());

        attachResponse(response.asString());
        logStep("Пользователи созданы");
    }
}