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
public class DeleteOrderTest extends ApiBaseTest {

    @Test
    @Story("Удаление заказа")
    public void testDeleteOrder() {
        logStep("1. Создаём питомца");

        Map<String, Object> newPet = TestDataGenerator.generateRandomPet();
        Response petResponse = RestAssured
                .given()
                .contentType("application/json")
                .body(newPet)
                .post("/pet");

        assertEquals(200, petResponse.statusCode());
        long petId = petResponse.jsonPath().getLong("id");

        logStep("2. Создаём заказ");
        Map<String, Object> newOrder = TestDataGenerator.generateOrder(petId);
        Response createResponse = RestAssured
                .given()
                .contentType("application/json")
                .body(newOrder)
                .post("/store/order");

        assertEquals(200, createResponse.statusCode());

        long orderId = createResponse.jsonPath().getLong("id");

        logStep("3. Удаляем заказ с ID: " + orderId);
        Response deleteResponse = RestAssured
                .given()
                .log().all()
                .delete("/store/order/" + orderId);

        int deleteStatus = deleteResponse.statusCode();
        assertTrue("Статус удаления должен быть 200 или 404",
                deleteStatus == 200 || deleteStatus == 404);

        logStep("4. Проверяем, что заказ удалён");
        Response getResponse = RestAssured
                .given()
                .get("/store/order/" + orderId);

        assertTrue("Заказ не должен существовать",
                getResponse.statusCode() == 404);

        attachResponse(getResponse.asString());
        logStep("Заказ успешно удалён");
    }
}