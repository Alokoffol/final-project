package com.example.base;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*
 - Базовый класс для тестов с базой данных.
 - Использует Testcontainers для поднятия реальной PostgreSQL в Docker.
 - Каждый тест получает чистое состояние через откат транзакций.
 */
public class DbBaseTest {

    private static final Logger log = LoggerFactory.getLogger(DbBaseTest.class);
    protected Connection connection;

    // Контейнер с PostgreSQL (один на все тесты)
    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withReuse(true); //withReuse(true): не пересоздавать контейнер между запусками,
    // ускоряет выполнение тестов после первого запуска

    /*
     - Статическая инициализация контейнера.
     - Выполняется один раз при загрузке класса.
    */
    static {
        // Фиксируем версию Docker API для совместимости
        System.setProperty("docker.client.api.version", "1.44");
        postgres.start();
        log.info("PostgreSQL контейнер запущен: {}", postgres.getJdbcUrl());
    }

    // Настройка перед каждым тестом
    @Before
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        connection.setAutoCommit(false); //autoCommit(false) отключает автосохранение. Позволяет делать rollback() после теста.
    }

    // Очистка после каждого теста
    @After
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.rollback(); // Откат всех изменений теста
            connection.close(); // Закрываем соединение
        }
    }

    // Вспомогательный метод
    protected void executeSql(String sql) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            log.debug("SQL выполнен: {}", sql);
        }
    }
}