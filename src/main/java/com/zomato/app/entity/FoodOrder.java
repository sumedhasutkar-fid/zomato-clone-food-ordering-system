package com.zomato.app.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class FoodOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;
    private Long addressId;
    private double totalAmount;
    private String status;
    private String paymentStatus;
    private LocalDateTime createdAt;

    public FoodOrder() {
    }

    public FoodOrder(String userEmail, Long addressId, double totalAmount) {
        this.userEmail = userEmail;
        this.addressId = addressId;
        this.totalAmount = totalAmount;
        this.status = "PLACED";
        this.paymentStatus = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getUserEmail() { return userEmail; }
    public Long getAddressId() { return addressId; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public String getPaymentStatus() { return paymentStatus; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}
