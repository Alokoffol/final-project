package com.example.service;

import com.example.models.unit.User;
import com.example.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    // Регистрирует нового пользователя
    public boolean registerUser(String email) {
        // Проверка email (простая валидация)
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Некорректный email: " + email);
        }

        // Проверяем, есть ли уже такой пользователь
        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            return false; // пользователь уже существует
        }

        // Создаём нового пользователя
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword("default123"); // в реальности пароль хешируют
        newUser.setActive(true);

        // Сохраняем
        userRepository.save(newUser);

        // Отправляем приветственное письмо
        emailService.sendWelcomeEmail(email);

        return true;
    }
}