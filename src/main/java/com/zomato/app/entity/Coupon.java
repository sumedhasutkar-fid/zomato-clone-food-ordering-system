package com.zomato.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;
    private double discountPercent;
    private boolean active;

    public Coupon() {
    }

    public Coupon(String code, double discountPercent) {
        this.code = code;
        this.discountPercent = discountPercent;
        this.active = true;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public double getDiscountPercent() { return discountPercent; }
    public boolean isActive() { return active; }
    public void setCode(String code) { this.code = code; }
    public void setDiscountPercent(double discountPercent) { this.discountPercent = discountPercent; }
    public void setActive(boolean active) { this.active = active; }
}
