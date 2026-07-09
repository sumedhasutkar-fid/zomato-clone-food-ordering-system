package com.zomato.app.entity;
import jakarta.persistence.*;

@Entity
@Table(name="foods")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
    private String imageUrl;
    private String description;
    private Long restaurantId;
    private Long categoryId;
    private boolean available = true;

    public Food() {}

    public Food(String name,double price,String imageUrl){
        this.name=name;
        this.price=price;
        this.imageUrl=imageUrl;
    }

    public Food(String name, double price, String imageUrl, String description, Long restaurantId, Long categoryId) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
        this.restaurantId = restaurantId;
        this.categoryId = categoryId;
        this.available = true;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
    public Long getRestaurantId() { return restaurantId; }
    public Long getCategoryId() { return categoryId; }
    public boolean isAvailable() { return available; }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setDescription(String description) { this.description = description; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public void setAvailable(boolean available) { this.available = available; }
}
