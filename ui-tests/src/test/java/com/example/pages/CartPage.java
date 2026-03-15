package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By cartItems = By.cssSelector(".cart_item");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public boolean isPageLoaded() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cartItems)).isDisplayed();
    }

    public int getItemCount() {
        return driver.findElements(cartItems).size();
    }

    public void clickCheckout() {
        driver.findElement(checkoutButton).click();
    }

    public void clickContinueShopping() {
        driver.findElement(continueShoppingButton).click();
    }
}