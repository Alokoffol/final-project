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
public class UpdateUserTest extends ApiBaseTest {

    @Test
    @Story("Изменяем пользователя")
    public void testUpdateUser() {
        logStep("1. Создаём пользователя");

        Map<String, Object> newUser = TestDataGenerator.generateRandomUser();
        String username = (String) newUser.get("username");

        Response createResponse = RestAssured
                .given()
                .contentType("application/json")
                .body(newUser)
                .post("/user");

        assertEquals(200, createResponse.statusCode());
        logStep("Пользователь создан: " + username);

        // 2. Получаем пользователя, чтобы узнать его ID
        logStep("2. Получаем данные пользователя");

        Response getBeforeResponse = RestAssured
                .given()
                .get("/user/" + username);

        assertEquals(200, getBeforeResponse.statusCode());
        long userId = getBeforeResponse.jsonPath().getLong("id");
        logStep("ID пользователя: " + userId);

        // 3. Обновляем пользователя
        logStep("3. Обновляем пользователя");

        // Создаём объект с обновлёнными данными и правильным ID
        Map<String, Object> updatedUser = Map.of(
                "id", userId,
                "username", username,
                "firstName", "Updated",
                "lastName", "Name",
                "email", (String) newUser.get("email"),
                "password", (String) newUser.get("password"),
                "phone", (String) newUser.get("phone"),
                "userStatus", 0
        );

        Response updateResponse = RestAssured
                .given()
                .log().all()
                .contentType("application/json")
                .body(updatedUser)
                .put("/user/" + username);

        assertEquals(200, updateResponse.statusCode());

        // 4. Проверяем обновление
        logStep("4. Проверяем обновлённые данные");

        Response getAfterResponse = RestAssured
                .given()
                .get("/user/" + username);

        assertEquals(200, getAfterResponse.statusCode());

        String updatedFirstName = getAfterResponse.jsonPath().getString("firstName");
        assertEquals("Updated", updatedFirstName);

        attachResponse(getAfterResponse.asString());
        logStep("Пользователь успешно обновлён");
    }
}