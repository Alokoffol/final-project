package com.example.tests;

import com.example.base.DbBaseTest;
import com.example.models.unit.User;
import com.example.repository.UserRepositoryImpl;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class UserRepositoryTest extends DbBaseTest {

    private UserRepositoryImpl userRepository;

    @Before
    public void init() throws SQLException {
        userRepository = new UserRepositoryImpl(connection);

        // Создаём таблицу перед тестами
        executeSql("CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY, " +
                "email VARCHAR(255) UNIQUE NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "first_name VARCHAR(100), " +
                "last_name VARCHAR(100), " +
                "phone VARCHAR(20), " +
                "active BOOLEAN DEFAULT true, " +
                "created_at TIMESTAMP, " +
                "updated_at TIMESTAMP" +
                ")");
    }

    @Test
    public void testSaveAndFindByEmail() {
        // Создаём пользователя
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("1234567890");
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Сохраняем
        userRepository.save(user);
        assertTrue("ID должен быть присвоен", user.getId() > 0);

        // Ищем по email
        User found = userRepository.findByEmail("test@example.com");
        assertNotNull(found);
        assertEquals(user.getEmail(), found.getEmail());
        assertEquals(user.getFirstName(), found.getFirstName());
    }

    @Test
    public void testUpdateUser() {
        // Создаём и сохраняем
        User user = new User();
        user.setEmail("update@example.com");
        user.setPassword("oldpass");
        user.setFirstName("Old");
        user.setLastName("Name");
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
        int userId = user.getId();

        // Обновляем
        user.setFirstName("New");
        user.setLastName("Updated");
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.update(user);

        // Проверяем
        User updated = userRepository.findByEmail("update@example.com");
        assertEquals("New", updated.getFirstName());
        assertEquals("Updated", updated.getLastName());
    }

    @Test
    public void testDeleteUser() {
        // Создаём
        User user = new User();
        user.setEmail("delete@example.com");
        user.setPassword("pass");
        user.setFirstName("To");
        user.setLastName("Delete");
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
        int userId = user.getId();

        // Удаляем
        userRepository.delete(userId);

        // Проверяем, что нет в БД
        User found = userRepository.findByEmail("delete@example.com");
        assertNull(found);
    }

    @Test
    public void testFindNonExistentUser() {
        User found = userRepository.findByEmail("nonexistent@example.com");
        assertNull(found);
    }
}