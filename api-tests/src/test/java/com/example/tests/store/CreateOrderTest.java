package com.example.tests.store;

import com.example.base.ApiBaseTest;
import com.example.utils.TestDataGenerator;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.*;

@Feature("Store")
public class CreateOrderTest extends ApiBaseTest {

    @Test
    @Story("Создание заказа")
    public void testCreateOrder() {
        logStep("1. Создаём питомца для заказа");

        // Создаём питомца
        Map<String, Object> newPet = TestDataGenerator.generateRandomPet();
        Response petResponse = RestAssured
                .given()
                .contentType("application/json")
                .body(newPet)
                .post("/pet");

        assertEquals(200, petResponse.statusCode());
        long petId = petResponse.jsonPath().getLong("id");
        logStep("Питомец создан с ID: " + petId);

        // Создаём заказ на этого питомца
        logStep("2. Создаём заказ");

        Map<String, Object> newOrder = TestDataGenerator.generateOrder(petId);
        Response createResponse = RestAssured
                .given()
                .log().all()
                .contentType("application/json")
                .body(newOrder)
                .post("/store/order");

        assertEquals(200, createResponse.statusCode());

        long orderId = createResponse.jsonPath().getLong("id");
        assertTrue("ID заказа должен быть положительным", orderId > 0);

        long returnedPetId = createResponse.jsonPath().getLong("petId");
        assertEquals(petId, returnedPetId);

        attachResponse(createResponse.asString());
        logStep("Заказ создан с ID: " + orderId);
    }
}