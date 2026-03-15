package com.example.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtils {

    private static final Logger log = LoggerFactory.getLogger(LoggerUtils.class);

    public static void logInfo(String message) {
        log.info("ℹ️ " + message);
    }

    public static void logDebug(String message) {
        log.debug("🔍 " + message);
    }

    public static void logError(String message, Throwable t) {
        log.error("❌ " + message, t);
    }

    public static void logStep(String stepName) {
        log.info("🟢 ШАГ: " + stepName);
    }

    public static void logRequest(String method, String url, Object body) {
        log.info("📤 {} {}", method, url);
        if (body != null) {
            log.debug("Тело запроса: {}", body);
        }
    }

    public static void logResponse(int status, Object body) {
        log.info("📥 Статус: {}", status);
        if (body != null) {
            log.debug("Тело ответа: {}", body);
        }
    }

    public static void logTestStart(String testName) {
        logSeparator();
        log.info("🚀 ТЕСТ: {}", testName);
        logSeparator();
    }

    public static void logTestEnd(String testName, boolean success) {
        if (success) {
            log.info("✅ ТЕСТ ПРОЙДЕН: {}", testName);
        } else {
            log.error("❌ ТЕСТ УПАЛ: {}", testName);
        }
        logSeparator();
    }

    public static void logSeparator() {
        log.info("=".repeat(50));
    }
}
