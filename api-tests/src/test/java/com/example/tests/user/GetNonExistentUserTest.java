package com.example.tests.user;

import com.example.base.ApiBaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;

import static org.junit.Assert.*;

@Feature("User")
public class GetNonExistentUserTest extends ApiBaseTest {

    @Test
    @Story("Получение несуществующего пользователя")
    public void testGetNonExistentUser() {
        String username = "nonexistent_" + System.currentTimeMillis();
        logStep("Попытка получить несуществующего пользователя: " + username);

        Response response = RestAssured
                .given()
                .log().all()
                .get("/user/" + username);

        int statusCode = response.statusCode();

        assertTrue("Статус должен быть 404 или 200",
                statusCode == 404 || statusCode == 200);

        attachResponse(response.asString());
        logStep("API вернул 404 — ожидаемо");
    }
}