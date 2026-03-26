package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private final WebDriver driver;

    // Локаторы элементов страницы
    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    // Методы для взаимодействия с формой
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterUsername(String username) {
        driver.findElement(usernameInput).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordInput).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    // Выполняет полный цикл авторизации.
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    // Получает текст сообщения об ошибке.
    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }

    // Проверяет, отображается ли сообщение об ошибке.
    public boolean isErrorMessageDisplayed() {
        return driver.findElements(errorMessage).size() > 0;
    }
}