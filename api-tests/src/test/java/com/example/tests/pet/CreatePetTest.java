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
public class CreatePetTest extends ApiBaseTest {

    @Test
    @Story("Создание питомца")
    public void testCreatePet() {
        logStep("Создание нового питомца через генератор");

        // 1. Генерируем питомца
        Map<String, Object> pet = TestDataGenerator.generateRandomPet();

        // 2. Отправляем запрос
        Response response = RestAssured
                .given()
                .log().all()
                .contentType("application/json")
                .body(pet)
                .post("/pet");

        assertEquals(200, response.statusCode());

        long petId = response.jsonPath().getLong("id");
        assertTrue("ID должен быть положительным", petId > 0);

        attachResponse(response.asString());
        logStep("Питомец создан с ID: " + petId);
    }
}