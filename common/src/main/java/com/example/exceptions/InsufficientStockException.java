package com.example.exceptions;

// Исключение «Недостаток запасов»
public class InsufficientStockException extends RuntimeException {

    // ID продукта: getId(), запрос(количество продуктов), доступно на складе: getStockQuantity()
    public InsufficientStockException(int productId, int requested, int available) {
        super(String.format(
                "Недостаточно товара. productId: %d, запрошено: %d, доступно: %d",
                productId, requested, available
        ));
    }
}