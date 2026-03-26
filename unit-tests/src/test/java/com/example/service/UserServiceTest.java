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
    private UserRepository userRepository;  // Мок репозитория (не ходим в БД)

    @Mock
    private EmailService emailService;      // Мок email-сервиса (не отправляем реальные письма)

    @InjectMocks
    private UserService userService;        // Тестируемый класс, моки внедряются автоматически

    // Пользователь с таким email еще не зарегистрирован.
    @Test
    public void testRegisterUser_success() {
        // GIVEN (Arrange) - подготовка данных
        String email = "new@example.com";

        // Настраиваем мок: пользователь с таким email НЕ найден
        when(userRepository.findByEmail(email)).thenReturn(null);

        // WHEN (Act) - выполняем тестируемый метод
        boolean result = userService.registerUser(email);

        // THEN (Assert) - проверяем результат
        assertTrue("Регистрация должна быть успешной", result);

        // Проверяем ВСЕ вызовы по порядку
        verify(userRepository).findByEmail(email);          // Поиск пользователя
        verify(userRepository).save(any(User.class));       // Сохранение нового пользователя
        verify(emailService).sendWelcomeEmail(email);       // Отправка приветственного письма

        // Убеждаемся, что больше никаких вызовов не было
        verifyNoMoreInteractions(userRepository, emailService);
    }

    // Пользователь с таким email уже зарегистрирован.
    @Test
    public void testRegisterUser_userAlreadyExists() {
        // 1. Given (дано)
        String email = "existing@example.com";
        User existingUser = new User();
        existingUser.setEmail(email);

        // Настраиваем мок: при поиске по этому email вернуть существующего пользователя
        when(userRepository.findByEmail(email)).thenReturn(existingUser);

        // 2. When (Assert)
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

    // Передан некорректный email (не содержит @)
    @Test(expected = IllegalArgumentException.class) // Ожидаем, что тест упадет с этим исключением
    public void testRegisterUser_invalidEmail_shouldThrowException() {
        // 1. Given (дано)
        String invalidEmail = "not-an-email"; // Невалидный email (нет символа @)

        // 2. When (когда) + Then (тогда)
        // Ожидаем, что метод выбросит IllegalArgumentException
        userService.registerUser(invalidEmail);

        // 3. Проверяем, что никакие методы моков НЕ вызывались
        verifyNoInteractions(userRepository, emailService);
    }
}