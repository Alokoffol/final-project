package com.example.service;

import com.example.models.unit.User;
import com.example.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;  // создаём мок репозитория

    @Mock
    private EmailService emailService;      // создаём мок email-сервиса

    @InjectMocks
    private UserService userService;        // создаём реальный сервис и ВНЕДРЯЕМ в него моки

    @Test
    public void testRegisterUser_success() {
        String email = "new@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        boolean result = userService.registerUser(email);

        assertTrue("Регистрация должна быть успешной", result);

        // Проверяем ВСЕ вызовы по порядку
        verify(userRepository).findByEmail(email);          // 1. поиск
        verify(userRepository).save(any(User.class));       // 2. сохранение
        verify(emailService).sendWelcomeEmail(email);       // 3. email

        verifyNoMoreInteractions(userRepository, emailService); // теперь можно
    }

    @Test
    public void testRegisterUser_userAlreadyExists() {
        // 1. Given (дано)
        String email = "existing@example.com";
        User existingUser = new User();
        existingUser.setEmail(email);

        // Настраиваем мок: при поиске по этому email вернуть существующего пользователя
        when(userRepository.findByEmail(email)).thenReturn(existingUser);

        // 2. When (когда)
        boolean result = userService.registerUser(email);

        // 3. Then (тогда)
        assertFalse("Регистрация не должна быть успешной", result);

        // Проверяем, что search был вызван
        verify(userRepository).findByEmail(email);

        // Проверяем, что save НЕ вызывался
        verify(userRepository, never()).save(any(User.class));

        // Проверяем, что email НЕ отправлялся
        verify(emailService, never()).sendWelcomeEmail(anyString());

        verifyNoMoreInteractions(userRepository, emailService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterUser_invalidEmail_shouldThrowException() {
        // 1. Given (дано)
        String invalidEmail = "not-an-email";

        // 2. When (когда) + Then (тогда)
        // Ожидаем, что метод выбросит IllegalArgumentException
        userService.registerUser(invalidEmail);

        // 3. Проверяем, что никакие методы моков НЕ вызывались
        verifyNoInteractions(userRepository, emailService);
    }
}