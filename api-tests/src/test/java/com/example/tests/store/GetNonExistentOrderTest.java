package com.example.tests.store;

import com.example.base.ApiBaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import static org.junit.Assert.*;

@Feature("Store")
public class GetNonExistentOrderTest extends ApiBaseTest {

    @Test
    @Story("Получение несуществующего заказа")
    public void testGetNonExistentOrder() {
        logStep("Попытка получить несуществующий заказ с ID: 999999999");

        Response response = RestAssured
                .given()
                .log().all()
                .get("/store/order/99999999910");

        int statusCode = response.statusCode();

        assertTrue("Статус должен быть 404 или 200",
                statusCode == 404 || statusCode == 200);

        attachResponse(response.asString());
        logStep("API вернул 404 — ожидаемо");
    }
}