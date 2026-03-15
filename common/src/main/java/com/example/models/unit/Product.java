package com.example.models.unit;

import com.example.exceptions.InsufficientStockException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {

    private int id;
    private String name; //название
    private String description; // описание
    private BigDecimal price; //цена
    private String category; // категория
    private int stockQuantity; // количество на складе
    private String imageUrl; // ссылка на картинку
    private boolean available; //доступен для заказа
    private LocalDateTime createdAt; // дата создания
    private LocalDateTime updatedAt; // дата обноваления

    public Product() {}

    public Product(String name, String description, BigDecimal price, String category,
                   int stockQuantity, String imageUrl, boolean available) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
        this.available = available;
        this.id = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }


    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public BigDecimal getPrice() {return price;}
    public void setPrice(BigDecimal price) {this.price = price;}

    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}

    public int getStockQuantity() {return stockQuantity;}
    public void setStockQuantity(int stockQuantity) {this.stockQuantity = stockQuantity;}

    public String getImageUrl() {return imageUrl;}
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    public boolean isAvailable() {return available;}
    public void setAvailable(boolean available) {this.available = available;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public LocalDateTime getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}

    // Проверяет есть ли товар на складе
    public boolean isInStock() {
        return getStockQuantity() > 0;
    }

    // Уменьшает остаток на складе после покупки
    public void decreaseStock(int quantity) {
        if (quantity > getStockQuantity()) {
            throw new InsufficientStockException(getId(), quantity, getStockQuantity());
        }
        setStockQuantity(getStockQuantity() - quantity);
    }

    // Увеличивает количество товара на складе после завоза или возврата
    public void increaseStock(int quantity) {
        setStockQuantity(getStockQuantity() + quantity);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", imageUrl='" + imageUrl + '\'' +
                ", available=" + available +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
