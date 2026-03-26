package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

public class ProductsPage {

    private final WebDriver driver;
    private final WebDriverWait wait; // Явные ожидания для стабильности

    // Локаторы элементов страницы
    private final By pageTitle = By.cssSelector(".title");
    private final By inventoryItems = By.cssSelector(".inventory_item");
    private final By addToCartButtons = By.cssSelector(".btn_inventory");
    private final By cartBadge = By.cssSelector(".shopping_cart_badge");
    private final By cartLink = By.cssSelector(".shopping_cart_link");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    // Получает заголовок страницы.
    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText();
    }

    // Проверяет, что страница загружена (заголовок видим).
    public boolean isPageLoaded() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).isDisplayed();
    }

    // Получает количество товаров на странице.
    public int getProductCount() {
        return driver.findElements(inventoryItems).size();
    }

    // Добавляет первый товар из списка в корзину.
    public void addFirstProductToCart() {
        List<WebElement> addButtons = driver.findElements(addToCartButtons);
        if (!addButtons.isEmpty()) {
            addButtons.get(0).click();
        }
    }

    // Добавляет товар в корзину по индексу.
    public void addProductToCartByIndex(int index) {
        List<WebElement> addButtons = driver.findElements(addToCartButtons);
        if (index >= 0 && index < addButtons.size()) {
            addButtons.get(index).click();
        }
    }

    // Получает количество товаров в корзине.
    public int getCartItemCount() {
        List<WebElement> badge = driver.findElements(cartBadge);
        if (badge.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(badge.get(0).getText());
    }

    // Переход в корзину.
    public void goToCart() {
        driver.findElement(cartLink).click();
    }
}