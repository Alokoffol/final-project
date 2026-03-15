package com.example.tests.store;

import com.example.base.ApiBaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.*;

@Feature("Store")
public class GetInventoryTest extends ApiBaseTest {

    @Test
    @Story("Получение инвентаря магазина")
    public void testGetInventory() {
        logStep("Получение инвентаря магазина");

        Response response = RestAssured
                .given()
                .log().all()
                .when()
                .get("/store/inventory")
                .then()
                .log().all()
                .extract().response();

        assertEquals(200, response.statusCode());

        Map<String, Integer> inventory = response.jsonPath().getMap("");
        assertNotNull("Инвентарь не должен быть null", inventory);
        assertFalse("Инвентарь не должен быть пустым", inventory.isEmpty());

        attachResponse(response.asString());
        logStep("Инвентарь получен");
    }
}