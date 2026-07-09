package com.zomato.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_addresses")
public class CustomerAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;
    private String label;
    private String line1;
    private String city;
    private String pincode;

    public CustomerAddress() {
    }

    public Long getId() { return id; }
    public String getUserEmail() { return userEmail; }
    public String getLabel() { return label; }
    public String getLine1() { return line1; }
    public String getCity() { return city; }
    public String getPincode() { return pincode; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public void setLabel(String label) { this.label = label; }
    public void setLine1(String line1) { this.line1 = line1; }
    public void setCity(String city) { this.city = city; }
    public void setPincode(String pincode) { this.pincode = pincode; }
}
