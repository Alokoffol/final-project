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
public class DeleteUserTest extends ApiBaseTest {

    @Test
    @Story("Удаление пользователя")
    public void testDeleteUser() {
        logStep("1. Создаём пользователя для удаления");

        Map<String, Object> newUser = TestDataGenerator.generateRandomUser();
        String username = (String) newUser.get("username");

        Response createResponse = RestAssured
                .given()
                .contentType("application/json")
                .body(newUser)
                .post("/user");

        assertEquals(200, createResponse.statusCode());

        logStep("2. Удаляем пользователя: " + username);
        Response deleteResponse = RestAssured
                .given()
                .log().all()
                .delete("/user/" + username);

        int statusCode = deleteResponse.statusCode();
        assertTrue("Статус должен быть 200 или 404",
                statusCode == 200 || statusCode == 404);

        logStep("3. Проверяем, что пользователь удалён");
        Response getResponse = RestAssured
                .given()
                .get("/user/" + username);

        assertTrue("Пользователь не должен существовать",
                getResponse.statusCode() == 404);

        attachResponse(getResponse.asString());
        logStep("Пользователь успешно удалён");
    }
}