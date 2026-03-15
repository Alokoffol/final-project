package com.example.models.unit;

import java.math.BigDecimal;

public class OrderItem {

    private int productId; //ID товара
    private String productName; //название товара (на момент заказа)
    private int quantity; //количество
    private BigDecimal price; //цена за единицу
    private BigDecimal totalPrice; //итого (price * quantity)

    public OrderItem() {}

    public OrderItem(int productId, String productName, int quantity, BigDecimal price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = price.multiply(BigDecimal.valueOf(quantity));
    }

    public int getProductId() {return productId;}
    public void setProductId(int productId) {this.productId = productId;}

    public String getProductName() {return productName;}
    public void setProductName(String productName) {this.productName = productName;}

    public int getQuantity() {return quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

    public BigDecimal getPrice() {return price;}
    public void setPrice(BigDecimal price) {this.price = price;}

    public BigDecimal getTotalPrice() {return totalPrice;}
    public void setTotalPrice(BigDecimal totalPrice) {this.totalPrice = totalPrice;}

    // пересчитывает общую сумму totalPrice
    public void calculateTotal() {
        BigDecimal quantityAsBigDecimal = BigDecimal.valueOf(this.quantity);
        this.totalPrice = this.price.multiply(quantityAsBigDecimal);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
