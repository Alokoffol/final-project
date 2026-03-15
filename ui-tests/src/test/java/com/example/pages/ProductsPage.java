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
    private final WebDriverWait wait;

    private final By pageTitle = By.cssSelector(".title");
    private final By inventoryItems = By.cssSelector(".inventory_item");
    private final By addToCartButtons = By.cssSelector(".btn_inventory");
    private final By cartBadge = By.cssSelector(".shopping_cart_badge");
    private final By cartLink = By.cssSelector(".shopping_cart_link");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText();
    }

    public boolean isPageLoaded() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).isDisplayed();
    }

    public int getProductCount() {
        return driver.findElements(inventoryItems).size();
    }

    public void addFirstProductToCart() {
        List<WebElement> addButtons = driver.findElements(addToCartButtons);
        if (!addButtons.isEmpty()) {
            addButtons.get(0).click();
        }
    }

    public void addProductToCartByIndex(int index) {
        List<WebElement> addButtons = driver.findElements(addToCartButtons);
        if (index >= 0 && index < addButtons.size()) {
            addButtons.get(index).click();
        }
    }

    public int getCartItemCount() {
        List<WebElement> badge = driver.findElements(cartBadge);
        if (badge.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(badge.get(0).getText());
    }

    public void goToCart() {
        driver.findElement(cartLink).click();
    }
}