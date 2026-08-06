package com.zomato.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "delivery_partners")
public class DeliveryPartner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String mobile;
    private boolean available;
    private double currentDistanceKm;

    public DeliveryPartner() {
    }

    public DeliveryPartner(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
        this.available = true;
        this.currentDistanceKm = 4.5;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getMobile() { return mobile; }
    public boolean isAvailable() { return available; }
    public double getCurrentDistanceKm() { return currentDistanceKm; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setCurrentDistanceKm(double currentDistanceKm) { this.currentDistanceKm = currentDistanceKm; }
}
