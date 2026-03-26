package com.example.driver;

import org.openqa.selenium.WebDriver;

// Менеджер для хранения WebDriver в ThreadLocal переменной
public class DriverManager {

    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    // Получить драйвер текущего потока.
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    // Сохранить драйвер для текущего потока.
    public static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }

    // Закрыть драйвер и удалить из ThreadLocal
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}