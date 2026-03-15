package com.example.tests;

import com.example.base.UiBaseTest;
import com.example.driver.DriverManager;
import com.example.pages.*;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Test;

import static org.junit.Assert.*;

@Feature("Полный путь покупки")
public class PurchaseTest extends UiBaseTest {

    @Test
    @Story("Полный путь покупки")
    public void testCompletePurchase() {
        logStep("1. Логин");
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login("standard_user", "secret_sauce");

        ProductsPage productsPage = new ProductsPage(DriverManager.getDriver());
        assertEquals("Products", productsPage.getPageTitle());

        logStep("2. Добавляем товар в корзину");
        productsPage.addFirstProductToCart();
        assertEquals(1, productsPage.getCartItemCount());

        logStep("3. Переходим в корзину");
        productsPage.goToCart();

        CartPage cartPage = new CartPage(DriverManager.getDriver());
        assertTrue(cartPage.isPageLoaded());
        assertEquals(1, cartPage.getItemCount());

        logStep("4. Начинаем оформление");
        cartPage.clickCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(DriverManager.getDriver());
        checkoutPage.fillCheckoutInfo("John", "Doe", "12345");

        logStep("5. Подтверждаем заказ");
        assertTrue(checkoutPage.isOverviewLoaded());
        checkoutPage.clickFinish();

        logStep("6. Проверяем успешное завершение");
        assertEquals("Thank you for your order!", checkoutPage.getCompleteHeader());

        logStep("✅ Покупка успешно завершена");
    }
}