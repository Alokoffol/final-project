package com.example.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

// Создание WebDriver в Docker окружении
public class DockerWebDriverFactory {

    private static final String GRID_URL = "http://172.18.0.3:5555/wd/hub";

    // Создает и возвращает WebDriver, подключенный к Selenium Grid.
    public static WebDriver createDriver() {
        ChromeOptions options = new ChromeOptions();
        options.setCapability("browserName", "chrome");
        options.setCapability("se:name", "Test in Docker"); //Имя теста для идентификации в Grid (полезно при параллельном запуске)

        try {
            // RemoteWebDriver вместо локального ChromeDriver
            // Все команды отправляются на Selenium Grid, который управляет Docker
            return new RemoteWebDriver(new URL(GRID_URL), options);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Ошибка URL для Selenium Grid", e);
        }
    }
}