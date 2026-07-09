package com.zomato.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;
    private Long foodId;
    private String foodName;
    private double price;
    private int quantity;

    public CartItem() {
    }

    public CartItem(String userEmail, Long foodId, String foodName, double price, int quantity) {
        this.userEmail = userEmail;
        this.foodId = foodId;
        this.foodName = foodName;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public String getUserEmail() { return userEmail; }
    public Long getFoodId() { return foodId; }
    public String getFoodName() { return foodName; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
