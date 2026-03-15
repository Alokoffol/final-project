package com.example.utils;

import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;

public class ConfigReader {
    private static Properties properties;
    private static String environment;

    // Статичный блок: пытается прочитать системную переменную env, если ее нет - берет dev
    static {
        environment = System.getProperty("env", "dev");
        loadProperties();
    }

    // Вернуть значение или null
    public static String get(String key) {
        return properties.getProperty(key);
    }

    // Дефолт
    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    // Преобразовать в int
    public static int getInt(String key) {
        String value = get(key);
        if (value == null) {
            throw new RuntimeException("Ключ не найден: " + key);
        }
        return Integer.parseInt(value);
    }

    // int с дефолтом
    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // В boolean
    public static boolean getBoolean(String key) {
        String value = get(key);
        if (value == null) {
            throw new RuntimeException("Ключ не найден: " + key);
        }
        return Boolean.parseBoolean(value);
    }

    // Нужен для случаев, когда параметр может отсутствовать в файле, а требуется получить значение по умолчанию безопасно
    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        }

        // Приводим к нижнему регистру для надёжности
        String lower = value.toLowerCase().trim();

        if ("true".equals(lower)) {
            return true;
        } else if ("false".equals(lower)) {
            return false;
        } else {
            return defaultValue;
        }
    }

    // Сменить окружение
    public static void setEnvironment(String env) {
        environment = env;
        loadProperties();
    }

    // Загрузка файла после статичного блока
    private static void loadProperties() {
        properties = new Properties();
        String fileName = "config-" + environment + ".properties";

        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException("Файл не найден: " + fileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки properties", e);
        }
    }
}
