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
    private String trackingStatus;
    private Long deliveryPartnerId;
    private double distanceKm;
    private LocalDateTime createdAt;

    public FoodOrder() {
    }

    public FoodOrder(String userEmail, Long addressId, double totalAmount) {
        this.userEmail = userEmail;
        this.addressId = addressId;
        this.totalAmount = totalAmount;
        this.status = "PLACED";
        this.paymentStatus = "PENDING";
        this.trackingStatus = "Restaurant received order";
        this.distanceKm = 4.5;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getUserEmail() { return userEmail; }
    public Long getAddressId() { return addressId; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public String getPaymentStatus() { return paymentStatus; }
    public String getTrackingStatus() { return trackingStatus; }
    public Long getDeliveryPartnerId() { return deliveryPartnerId; }
    public double getDistanceKm() { return distanceKm; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setStatus(String status) { this.status = status; }
    public void setTrackingStatus(String trackingStatus) { this.trackingStatus = trackingStatus; }
    public void setDeliveryPartnerId(Long deliveryPartnerId) { this.deliveryPartnerId = deliveryPartnerId; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }
}
