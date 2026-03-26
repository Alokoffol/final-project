package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class CheckoutPage {

    private final WebDriver driver;
    private final WebDriverWait wait; // Явные ожидания для стабильности

    // Локаторы элементов страницы
    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By finishButton = By.id("finish");
    private final By completeHeader = By.cssSelector(".complete-header");
    private final By cancelButton = By.id("cancel");

    // Конструктор инициализирует драйвер и явные ожидания.
    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    //==== Методы для ввода данных ====
    public void enterFirstName(String firstName) {
        driver.findElement(firstNameInput).sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        driver.findElement(lastNameInput).sendKeys(lastName);
    }

    public void enterPostalCode(String postalCode) {
        driver.findElement(postalCodeInput).sendKeys(postalCode);
    }

    // Заполняет все поля формы и нажимает "Continue".
    public void fillCheckoutInfo(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        clickContinue();
    }

    //==== Методы для нажатия кнопок ====
    public void clickContinue() {
        driver.findElement(continueButton).click();
    }

    public void clickFinish() {
        driver.findElement(finishButton).click();
    }

    public void clickCancel() {
        driver.findElement(cancelButton).click();
    }

    // Получает текст заголовка после успешного заказа.
    public String getCompleteHeader() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(completeHeader)).getText();
    }

    // Проверяет, что страница обзора заказа загружена.
    public boolean isOverviewLoaded() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(finishButton)).isDisplayed();
    }
}