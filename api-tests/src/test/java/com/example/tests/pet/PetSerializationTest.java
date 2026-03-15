package com.example.tests.pet;

import com.example.base.ApiBaseTest;
import com.example.models.api.Pet;
import com.example.utils.TestDataGenerator;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;

import static org.junit.Assert.*;

@Feature("Pet (Serialization Demo)")
public class PetSerializationTest extends ApiBaseTest {

    @Test
    @Story("Сериализация/десериализация через POJO")
    public void testPetSerialization() {
        logStep("1. Создаём объект Pet через генератор");

        // СЕРИАЛИЗАЦИЯ: объект Pet → JSON
        Pet newPet = TestDataGenerator.generateRandomPetAsObject();
        logStep("Создан объект: " + newPet);

        // Отправляем запрос
        Response response = RestAssured
                .given()
                .log().all()
                .contentType("application/json")
                .body(newPet)  // ← Pet автоматически превратится в JSON
                .post("/pet");

        assertEquals(200, response.statusCode());

        // ДЕСЕРИАЛИЗАЦИЯ: JSON → объект Pet
        Pet createdPet = response.as(Pet.class);
        logStep("Получен объект из ответа: " + createdPet);

        // Проверяем, что данные совпадают
        assertNotNull("ID не должен быть null", createdPet.getId());
        assertTrue("ID должен быть положительным", createdPet.getId() > 0);

        // Сравниваем поля (кроме ID, он мог измениться)
        assertEquals(newPet.getName(), createdPet.getName());
        assertEquals(newPet.getStatus(), createdPet.getStatus());

        attachResponse(response.asString());
        logStep("Тест сериализации/десериализации пройден");
    }
}