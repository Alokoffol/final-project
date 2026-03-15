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
public class GetOrderTest extends ApiBaseTest {

    @Test
    @Story("Получение заказа")
    public void testCreateAndGetOrder() {
        logStep("1. Создаём заказ");

        // 1. Сначала создадим питомца (чтобы было на кого делать заказ)
        Map<String, Object> newPet = TestDataGenerator.generateRandomPet();
        Response petResponse = RestAssured
                .given()
                .contentType("application/json")
                .body(newPet)
                .post("/pet");

        assertEquals(200, petResponse.statusCode());
        long petId = petResponse.jsonPath().getLong("id");
        logStep("Питомец создан с ID: " + petId);

        // 2. Создаём заказ на этого питомца
        Map<String, Object> newOrder = TestDataGenerator.generateOrder(petId);
        Response createResponse = RestAssured
                .given()
                .contentType("application/json")
                .body(newOrder)
                .post("/store/order");

        assertEquals(200, createResponse.statusCode());
        long orderId = createResponse.jsonPath().getLong("id");
        logStep("Заказ создан с ID: " + orderId);

        // 3. Получаем этот заказ
        logStep("3. Получаем заказ с ID: " + orderId);

        Response getResponse = RestAssured
                .given()
                .log().all()
                .get("/store/order/" + orderId);

        assertEquals(200, getResponse.statusCode());

        long returnedOrderId = getResponse.jsonPath().getLong("id");
        assertEquals(orderId, returnedOrderId);

        long returnedPetId = getResponse.jsonPath().getLong("petId");
        assertEquals(petId, returnedPetId);

        attachResponse(getResponse.asString());
        logStep("Заказ успешно получен");
    }
}