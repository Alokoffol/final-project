package com.example.tests;

import com.example.base.UiBaseTest;
import com.example.driver.DriverManager;
import com.example.pages.LoginPage;
import com.example.pages.ProductsPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Test;

import static org.junit.Assert.*;

@Feature("Логин")
public class LoginTest extends UiBaseTest {

    @Test
    @Story("Успешный логин")
    public void testSuccessfulLogin() {
        logStep("Тест успешного логина");

        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login("standard_user", "secret_sauce");

        ProductsPage productsPage = new ProductsPage(DriverManager.getDriver());
        assertEquals("Products", productsPage.getPageTitle());

        logStep("Успешный вход выполнен");
    }

    @Test
    @Story("Неверный логин")
    public void testInvalidLogin() {
        logStep("Тест с неверными данными");

        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login("wrong_user", "wrong_pass");

        assertTrue(loginPage.isErrorMessageDisplayed());
        assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"));

        logStep("Ошибка отобразилась корректно");
    }

    @Test
    @Story("Пустые поля логин и пароль")
    public void testEmptyFields() {
        logStep("Тест с пустыми полями");

        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.clickLogin();

        assertTrue(loginPage.isErrorMessageDisplayed());
        assertTrue(loginPage.getErrorMessage().contains("Username is required"));

        logStep("Ошибка отобразилась корректно");
    }
}