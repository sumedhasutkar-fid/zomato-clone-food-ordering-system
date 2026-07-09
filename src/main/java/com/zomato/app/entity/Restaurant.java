package com.zomato.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String cuisine;
    private String location;
    private String imageUrl;
    private double rating;

    public Restaurant() {
    }

    public Restaurant(String name, String cuisine, String location, String imageUrl, double rating) {
        this.name = name;
        this.cuisine = cuisine;
        this.location = location;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCuisine() { return cuisine; }
    public String getLocation() { return location; }
    public String getImageUrl() { return imageUrl; }
    public double getRating() { return rating; }

    public void setName(String name) { this.name = name; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }
    public void setLocation(String location) { this.location = location; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setRating(double rating) { this.rating = rating; }
}
