package com.example.repository;

import com.example.models.unit.User;

public interface UserRepository {
    User findByEmail(String email);
    void save(User user);
    void update(User user);      // ← добавить
    void delete(int id);          // ← добавить
}