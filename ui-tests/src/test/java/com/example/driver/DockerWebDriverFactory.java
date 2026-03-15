package com.example.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

// Создание драйвера для докера
public class DockerWebDriverFactory {

    private static final String GRID_URL = "http://172.18.0.3:5555/wd/hub";

    public static WebDriver createDriver() {
        ChromeOptions options = new ChromeOptions();
        options.setCapability("browserName", "chrome");
        options.setCapability("se:name", "Test in Docker");

        try {
            return new RemoteWebDriver(new URL(GRID_URL), options);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Ошибка URL для Selenium Grid", e);
        }
    }
}