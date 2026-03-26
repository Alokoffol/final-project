package com.example.repository;

import com.example.models.unit.User;

public interface UserRepository {
    User findByEmail(String email);
    void save(User user);
    void update(User user);
    void delete(int id);
}