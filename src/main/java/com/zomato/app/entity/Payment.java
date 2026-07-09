package com.zomato.app.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private String provider;
    private String status;
    private String transactionId;
    private LocalDateTime paidAt;

    public Payment() {
    }

    public Payment(Long orderId, String provider, String status, String transactionId) {
        this.orderId = orderId;
        this.provider = provider;
        this.status = status;
        this.transactionId = transactionId;
        this.paidAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getOrderId() { return orderId; }
    public String getProvider() { return provider; }
    public String getStatus() { return status; }
    public String getTransactionId() { return transactionId; }
    public LocalDateTime getPaidAt() { return paidAt; }
}
