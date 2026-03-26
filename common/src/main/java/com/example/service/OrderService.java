package com.example.service;

import com.example.models.unit.Order;
import com.example.models.unit.OrderItem;
import com.example.models.unit.OrderStatus;
import com.example.models.unit.Product;
import com.example.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderService {

    private final OrderRepository orderRepository; // Репозиторий для работы с БД
    private final ProductService productService; // Сервис для работы с товарами
    private static final BigDecimal TAX_RATE = new BigDecimal("0.20"); // 20% налог

    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    // Создаёт новый заказ
    public Order createOrder(int userId, List<OrderItem> items) {
        // Проверка на пустой заказ
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Заказ не может быть пустым");
        }

        // Проверка наличия всех товаров на складе
        for (OrderItem item : items) {
            int productId = item.getProductId();
            int quantity = item.getQuantity();

            if (!productService.isProductInStock(productId, quantity)) {
                throw new IllegalStateException(
                        "Товар с ID " + productId + " отсутствует в нужном количестве"
                );
            }
        }

        // Создание заказа
        Order order = new Order();
        order.setUserId(userId);
        order.setItems(items);
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Расчёт суммы
        BigDecimal subtotal = calculateSubtotal(items);
        BigDecimal tax = calculateTax(subtotal);
        BigDecimal total = subtotal.add(tax);

        order.setTotalAmount(total);

        // Сохраняем заказ
        Order savedOrder = orderRepository.save(order);

        // Уменьшаем остатки на складе
        for (OrderItem item : items) {
            productService.decreaseStock(item.getProductId(), item.getQuantity());
        }

        return savedOrder;
    }

    // Отменить заказ
    public boolean cancelOrder(int orderId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    if (order.getStatus() == OrderStatus.NEW) {
                        order.setStatus(OrderStatus.CANCELLED);
                        order.setUpdatedAt(LocalDateTime.now());
                        orderRepository.save(order);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    // Расчет промежуточного итога
    private BigDecimal calculateSubtotal(List<OrderItem> items) {
        return items.stream()
                .map(item -> {
                    Product product = productService.getProductById(item.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Товар с ID " + item.getProductId() + " не найден"
                            ));
                    return product.getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Рассчитать налог
    private BigDecimal calculateTax(BigDecimal subtotal) {
        return subtotal.multiply(TAX_RATE);
    }
}