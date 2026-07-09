package com.zomato.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long foodId;
    private String foodName;
    private double price;
    private int quantity;

    public OrderItem() {
    }

    public OrderItem(Long orderId, Long foodId, String foodName, double price, int quantity) {
        this.orderId = orderId;
        this.foodId = foodId;
        this.foodName = foodName;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public Long getOrderId() { return orderId; }
    public Long getFoodId() { return foodId; }
    public String getFoodName() { return foodName; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
}
