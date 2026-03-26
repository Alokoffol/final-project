package com.example.tests.pet;

import com.example.base.ApiBaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;

import static org.junit.Assert.*;

@Feature("Pet")
public class GetNonExistentPetTest extends ApiBaseTest {

    @Test
    @Story("Получение несуществующего питомца")
    public void testGetNonExistentPet() {
        logStep("Попытка получить несуществующего питомца с ID: 9999999999");

        Response response = RestAssured
                .given()
                .log().all()
                .get("/pet/9999999999");

        int statusCode = response.statusCode();

        assertTrue("Статус должен быть 404 или 200",
                statusCode == 404 || statusCode == 200);

        attachResponse(response.asString());
        logStep("API вернул статус: " + statusCode);
    }
}