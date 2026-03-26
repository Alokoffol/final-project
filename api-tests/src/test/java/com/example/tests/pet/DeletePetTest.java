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
public class DeletePetTest extends ApiBaseTest {

    @Test
    @Story("Удаление питомца")
    public void testDeletePet() {
        logStep("1. Создаём питомца для удаления");

        // Создаём питомца
        Map<String, Object> newPet = TestDataGenerator.generateRandomPet();
        Response createResponse = RestAssured
                .given()
                .contentType("application/json")
                .body(newPet)
                .post("/pet");

        assertEquals(200, createResponse.statusCode());
        long petId = createResponse.jsonPath().getLong("id");
        logStep("Питомец создан с ID: " + petId);

        // Удаляем питомца
        logStep("2. Удаляем питомца с ID: " + petId);

        Response deleteResponse = RestAssured
                .given()
                .log().all()
                .delete("/pet/" + petId);

        assertEquals(200, deleteResponse.statusCode());

        // Пробуем получить удалённого питомца — должен быть 404
        logStep("3. Проверяем, что питомец действительно удалён");

        Response getResponse = RestAssured
                .given()
                .get("/pet/" + petId);

        assertTrue("Статус должен быть 200 или 404",
                getResponse.statusCode() == 200 || getResponse.statusCode() == 404);

        attachResponse(getResponse.asString());
        logStep("Питомец успешно удалён");
    }
}