package com.example.tests.pet;

import com.example.base.ApiBaseTest;
import com.example.utils.TestDataGenerator;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.*;

@Feature("Pet")
public class GetPetTest extends ApiBaseTest {

    @Test
    @Story("Получение питомца по id")
    public void testCreateAndGetPet() {
        logStep("1. Создаём питомца");

        // 1. Создаём питомца
        Map<String, Object> newPet = TestDataGenerator.generateRandomPet();
        Response createResponse = RestAssured
                .given()
                .contentType("application/json")
                .body(newPet)
                .post("/pet");

        assertEquals(200, createResponse.statusCode());
        long petId = createResponse.jsonPath().getLong("id");
        logStep("Питомец создан с ID: " + petId);

        // 2. Получаем этого же питомца
        logStep("2. Получаем питомца с ID: " + petId);

        Response getResponse = RestAssured
                .given()
                .log().all()
                .get("/pet/" + petId);

        assertEquals(200, getResponse.statusCode());

        long returnedId = getResponse.jsonPath().getLong("id");
        assertEquals(petId, returnedId);

        attachResponse(getResponse.asString());
        logStep("Питомец успешно получен");
    }
}