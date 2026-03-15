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

    /**
     * Регистрирует нового пользователя
     * @param email email пользователя
     * @return true если пользователь создан, false если уже существует
     * @throws IllegalArgumentException если email некорректный
     */
    public boolean registerUser(String email) {
        // 1. Проверка email (простая валидация)
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Некорректный email: " + email);
        }

        // 2. Проверяем, есть ли уже такой пользователь
        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            return false; // пользователь уже существует
        }

        // 3. Создаём нового пользователя
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword("default123"); // в реальности пароль хешируют
        newUser.setActive(true);

        // 4. Сохраняем
        userRepository.save(newUser);

        // 5. Отправляем приветственное письмо
        emailService.sendWelcomeEmail(email);

        return true;
    }
}