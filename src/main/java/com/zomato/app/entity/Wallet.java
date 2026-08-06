package com.zomato.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userEmail;
    private double balance;

    public Wallet() {
    }

    public Wallet(String userEmail, double balance) {
        this.userEmail = userEmail;
        this.balance = balance;
    }

    public Long getId() { return id; }
    public String getUserEmail() { return userEmail; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}
