package com.zomato.app.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class NotificationMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;
    private String message;
    private boolean readStatus;
    private LocalDateTime createdAt;

    public NotificationMessage() {
    }

    public NotificationMessage(String userEmail, String message) {
        this.userEmail = userEmail;
        this.message = message;
        this.readStatus = false;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getUserEmail() { return userEmail; }
    public String getMessage() { return message; }
    public boolean isReadStatus() { return readStatus; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
