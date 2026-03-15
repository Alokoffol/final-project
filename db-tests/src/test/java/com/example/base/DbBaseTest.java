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

public class DbBaseTest {

    private static final Logger log = LoggerFactory.getLogger(DbBaseTest.class);
    protected Connection connection;
    // Контейнер с PostgreSQL (один на все тесты)
    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withReuse(true);

    static {
        // Принудительно указываем версию API
        System.setProperty("docker.client.api.version", "1.44");
        postgres.start();
        log.info("PostgreSQL контейнер запущен: {}", postgres.getJdbcUrl());
    }

    @Before
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        connection.setAutoCommit(false);
    }

    @After
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.rollback();
            connection.close();
        }
    }

    protected void executeSql(String sql) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }
}