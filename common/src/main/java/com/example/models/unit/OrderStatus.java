package com.example.models.unit;

public enum OrderStatus {

    NEW("Новый заказ"),
    PROCESSING("В обработке"),
    COMPLETED("Выполнен"),
    CANCELLED("Отменён"),
    REFUNDED("Возврат");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // Возвращает true для активных статусов (можно изменять/отменять)
    public boolean isActive() {
        return this == NEW || this == PROCESSING;
    }

    // Возвращает true для завершающих статусов (нельзя менять)
    public boolean isFinal() {
        return this == COMPLETED || this == CANCELLED || this == REFUNDED;
    }
}