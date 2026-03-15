package com.example.repository;

import com.example.models.unit.Order;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(int id);
    void deleteById(int id);
}