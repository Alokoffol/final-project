package com.example.service;

import com.example.models.unit.Product;
import java.util.Optional;

public interface ProductService {
    Optional<Product> getProductById(int productId);
    boolean isProductInStock(int productId, int quantity);
    void decreaseStock(int productId, int quantity);
}