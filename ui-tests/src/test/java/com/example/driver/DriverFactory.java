package com.example.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

// Локальный или докер
public class DriverFactory {

    public static WebDriver createDriver(boolean headless, boolean useDocker) {
        if (useDocker) {
            return DockerWebDriverFactory.createDriver();
        }

        // Обычный локальный драйвер
        WebDriverManager.chromedriver().clearDriverCache().clearResolutionCache();
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(ChromeOptionsConfig.createChromeOptions(headless));
    }

    public static void quitDriver(WebDriver driver) {
        if (driver != null) {
            driver.quit();
        }
    }
}