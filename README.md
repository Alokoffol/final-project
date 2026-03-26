# MavenMulti-moduleProject

Мультимодульный Maven проект для автоматизации тестирования.  
Демонстрирует навыки написания unit, API, UI и интеграционных тестов с использованием современного стека инструментов.

## Технологии и инструменты

| Категория | Технологии |
|-----------|------------|
| **Язык** | Java 11 |
| **Сборка** | Maven (мультимодульная структура) |
| **Unit тесты** | JUnit 4, Mockito |
| **API тесты** | REST Assured, Jackson |
| **UI тесты** | Selenium WebDriver, Page Object Pattern |
| **Интеграционные тесты** | Testcontainers, JDBC, PostgreSQL |
| **Отчеты** | Allure Framework, SLF4J |
| **CI/CD** | GitHub Actions |
| **Контейнеризация** | Docker (Testcontainers) |

---

## 📁 Структура проекта

MavenMulti-moduleProject/
├── common/ # Общие модели, утилиты, сервисы
├── api/ # API тесты (REST Assured)
├── ui/ # UI тесты (Selenium, Page Object)
├── db-tests/ # Интеграционные тесты (Testcontainers, PostgreSQL)
├── unit-tests/ # Unit тесты (JUnit, Mockito)
├── .github/workflows/ # GitHub Actions CI/CD
└── pom.xml # Родительский POM (Maven)

## Запуск тестов

### Локальный запуск
```bash
# Все тесты
mvn clean test

# Только API тесты
mvn clean test -pl api

# Только UI тесты
mvn clean test -pl ui

# Только DB тесты
mvn clean test -pl db-tests

# После запуска тестов
allure generate --clean -o allure-report allure-results
allure open allure-report


CI/CD (GitHub Actions)

При каждом пуше в ветку main:
    Автоматически запускаются все тесты
    Собираются Allure результаты
    Генерируется отчет
    Отчет публикуется на GitHub Pages

Что демонстрирует проект

    - Мультимодульную архитектуру Maven
    - Unit тестирование с моками (Mockito)
    - API тестирование (REST Assured, Jackson)
    - UI тестирование (Selenium, Page Object)
    - Интеграционное тестирование с реальной БД (Testcontainers)
    - Настройку CI/CD (GitHub Actions)
    - Генерацию отчетов (Allure)
    - Логирование (SLF4J)
    - Работу с базами данных (JDBC, PostgreSQL)


