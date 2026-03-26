package com.example.repository;

import com.example.models.unit.User;
import java.sql.*;

/*
 - Реализация репозитория для работы с пользователями в БД.
 - Выполняет CRUD операции: поиск, сохранение, обновление, удаление.
 - Использует JDBC для прямого взаимодействия с базой данных.
 */
public class UserRepositoryImpl implements UserRepository {

    // JDBC соединение с БД
    private final Connection connection;

    /*
     - Конструктор принимает готовое соединение.
     - Это позволяет использовать один и тот же connection для разных репозиториев
     - и управлять транзакциями централизованно.
     */
    public UserRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    // Поиск пользователя по email.
    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapRowToUser(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске пользователя", e);
        }
        return null;
    }

    // Сохранение нового пользователя в БД.
    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (email, password, first_name, last_name, phone, active, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getPhone());
            stmt.setBoolean(6, user.isActive());
            stmt.setTimestamp(7, Timestamp.valueOf(user.getCreatedAt()));
            stmt.setTimestamp(8, Timestamp.valueOf(user.getUpdatedAt()));

            stmt.executeUpdate();

            // Получаем сгенерированный ID
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении пользователя", e);
        }
    }

    // Обновление существующего пользователя.
    @Override
    public void update(User user) {
        String sql = "UPDATE users SET email = ?, password = ?, first_name = ?, last_name = ?, " +
                "phone = ?, active = ?, updated_at = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getPhone());
            stmt.setBoolean(6, user.isActive());
            stmt.setTimestamp(7, Timestamp.valueOf(user.getUpdatedAt()));
            stmt.setInt(8, user.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении пользователя", e);
        }
    }

    // Удаление пользователя по ID.
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении пользователя", e);
        }
    }


    /*
    - Маппинг строки ResultSet в объект User
    - Вынесен в отдельный метод для переиспользования.
    */
    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setPhone(rs.getString("phone"));
        user.setActive(rs.getBoolean("active"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return user;
    }
}