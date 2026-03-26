package com.example.base;

import com.example.driver.DriverFactory;
import com.example.driver.DriverManager;
import com.example.utils.LoggerUtils;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

//Базовый класс для всех UI тестов.
public class UiBaseTest {

    protected static final String BASE_URL = "https://www.saucedemo.com";

    // Настройки запуска (можно передавать через системные свойства)
    // Пример: mvn test -Dheadless=true -Ddocker=false
    protected boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
    protected boolean useDocker = Boolean.parseBoolean(System.getProperty("docker", "false"));

    /*
     - TestWatcher - JUnit правило, которое срабатывает при падении теста.
       При падении:
     - Делает скриншот страницы
     - Логирует ошибку
     - Скриншот автоматически прикрепляется к Allure отчету
     */
    @Rule
    public TestWatcher watcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            // Получаем драйвер из DriverManager
            WebDriver driver = DriverManager.getDriver();
            if (driver != null) {
                takeScreenshot(); // Скриншот попадет в Allure через @Attachment
                LoggerUtils.logError("Тест упал: " + description.getMethodName(), e);
            }
        }
    };

    /* Инициализация браузера перед каждым тестом.
     - Логируем начало теста и режим запуска
     - Создаем WebDriver через DriverFactory
     - Сохраняем драйвер в DriverManager (для доступа в других классах)
     - Настраиваем окно и открываем страницу
     */
    @Before
    public void setUp() {
        LoggerUtils.logTestStart(this.getClass().getSimpleName());
        LoggerUtils.logStep("Режим: " + (useDocker ? "Docker" : "локальный") +
                (headless ? " (headless)" : ""));

        WebDriver driver = DriverFactory.createDriver(headless, useDocker);
        DriverManager.setDriver(driver);
        driver.manage().window().maximize();
        driver.get(BASE_URL);

        logStep("Браузер запущен, страница загружена");
    }

    @After
    public void tearDown() {
        DriverManager.quitDriver();
        logStep("Браузер закрыт");
        LoggerUtils.logTestEnd(this.getClass().getSimpleName(), true);
    }

    @Step("{0}")
    protected void logStep(String message) {
        LoggerUtils.logStep(message);
    }

    // Создание скриншота и прикрепление к Allure отчету.
    @Attachment(value = "Скриншот при падении", type = "image/png")
    public byte[] takeScreenshot() {
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        }
        return null;
    }
}