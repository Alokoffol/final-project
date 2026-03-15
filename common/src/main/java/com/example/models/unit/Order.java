package com.example.models.unit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private int id; // номер заказа
    private int userId; // кто заказал
    private List<OrderItem> items; // список товаров
    private BigDecimal totalAmount; // общая сумма
    private OrderStatus status; //статус заказа
    private String paymentMethod; // способ оплаты
    private String deliveryAddress; // адрес доставки
    private LocalDateTime createdAt; // время создания
    private LocalDateTime updatedAt; // время обновления

    public Order() {}

    public Order(int userId, List<OrderItem> items, BigDecimal totalAmount,
                 OrderStatus status, String paymentMethod, String deliveryAddress) {
        this.id = 0;
        this.userId = userId;
        this.items = new ArrayList<>();;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.deliveryAddress = deliveryAddress;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public int getUserId() {return userId;}
    public void setUserId(int userId) {this.userId = userId;}

    public List<OrderItem> getItems() {return items;}
    public void setItems(List<OrderItem> items) {this.items = items;}

    public BigDecimal getTotalAmount() {return totalAmount;}
    public void setTotalAmount(BigDecimal totalAmount) {this.totalAmount = totalAmount;}

    public OrderStatus getStatus() {return status;}
    public void setStatus(OrderStatus status) {this.status = status;}

    public String getPaymentMethod() {return paymentMethod;}
    public void setPaymentMethod(String paymentMethod) {this.paymentMethod = paymentMethod;}

    public String getDeliveryAddress() {return deliveryAddress;}
    public void setDeliveryAddress(String deliveryAddress) {this.deliveryAddress = deliveryAddress;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public LocalDateTime getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}

    // Добавляет товар в список
    public void addItem(OrderItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }

        // Проверяем, есть ли уже такой товар в заказе
        for (OrderItem existingItem : items) {
            if (existingItem.getProductId() == item.getProductId()) {
                // Если уже есть — увеличиваем количество
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                existingItem.calculateTotal();
                return;
            }
        }

        // Если товара ещё нет — добавляем новый
        items.add(item);
    }

    // Удаляет товар по id
    public void removeItem(int productId) {
        if (items == null) return;

        items.removeIf(item -> item.getProductId() == productId);
    }

    // Пересчитывает общую сумму на основе items
    public void calculateTotal() {
        if (items == null || items.isEmpty()) {
            this.totalAmount = BigDecimal.ZERO;
            return;
        }

        BigDecimal sum = BigDecimal.ZERO;

        for (OrderItem item : items) {
            sum = sum.add(item.getTotalPrice());
        }

        this.totalAmount = sum;
    }

    // можно ли отменить заказ
    public boolean isCancellable() {
        return this.status == OrderStatus.NEW;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", items=" + items +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
